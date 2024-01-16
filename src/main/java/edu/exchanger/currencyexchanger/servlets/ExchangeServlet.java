package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.dto.ExchangeDTO;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import edu.exchanger.currencyexchanger.repositories.ExchangeRatesRepository;
import edu.exchanger.currencyexchanger.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private ExchangeRatesRepository exchangeRatesRepository;

    @Override
    public void init() throws ServletException {
        currencyRepository = new CurrencyRepository();
        exchangeRatesRepository = new ExchangeRatesRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (Util.isNotValidExchangeArgs(from, to, amount) || !Util.isStringDouble(amount)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неправильно введен запрос. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        if (!isCurrenciesValid(from, to)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Указана не существующая валюта. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        BigDecimal rate = getRate(from, to);

        if (rate == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Не существует курс обмена");
            return;
        }

        new ObjectMapper().writeValue(resp.getWriter(), new ExchangeDTO(
                currencyRepository.findByCode(from).get(),
                currencyRepository.findByCode(to).get(),
                rate,
                new BigDecimal(amount),
                rate.multiply(new BigDecimal(amount))
        ));
    }
    private boolean isCurrenciesValid(String from, String to) {
        Optional<Currency> fromCurrency = currencyRepository.findByCode(from);
        Optional<Currency> toCurrency = currencyRepository.findByCode(to);

        return (fromCurrency.isPresent() && toCurrency.isPresent());
    }

    private BigDecimal getRate(String from, String to) {
        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(from, to);

        if (exchangeRate.isPresent())
            return exchangeRate.get().getRate();

        Optional<ExchangeRate> reverseExchangeRate = exchangeRatesRepository.findByCodes(to, from);

        if (reverseExchangeRate.isPresent())
            return reverseExchangeRate.get().getRate();

        Optional<ExchangeRate> exchangeRateUSD_A = exchangeRatesRepository.findByCodes("USD", from);
        Optional<ExchangeRate> exchangeRateUSD_B = exchangeRatesRepository.findByCodes("USD", to);

        if (exchangeRateUSD_A.isPresent() && exchangeRateUSD_B.isPresent()) {
            return exchangeRateUSD_A.get().getRate().divide(exchangeRateUSD_B.get().getRate());
        }

        return null;
    }

}
