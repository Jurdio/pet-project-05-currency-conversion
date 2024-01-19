package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.domain.Exchange;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.dto.ExchangeDTO;
import edu.exchanger.currencyexchanger.mappers.ExchangeMapper;
import edu.exchanger.currencyexchanger.mappers.ExchangeMapperImpl;
import edu.exchanger.currencyexchanger.services.ExchangeService;
import edu.exchanger.currencyexchanger.domain.Currency;
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
    CurrencyRepository currencyRepository;
    ExchangeRatesRepository exchangeRatesRepository;

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

        Optional<ExchangeRate> exchangeRate = findExchangeRate(from,to);

        if (exchangeRate.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Указана не существующая валюта. Пример: /exchange?from=USD&to=RUB&amount=10");
            return;
        }

        ExchangeService exchangeService = ExchangeService.builder()
                .exchangeRate(exchangeRate.get())
                .amount(BigDecimal.valueOf(Double.parseDouble(amount)))
                .build();

        ExchangeDTO exchangeDTO = ExchangeMapper.INSTANCE.toExchangeDTO(exchangeService.getExchange());


        new ObjectMapper().writeValue(resp.getWriter(),exchangeDTO);

    }
    private boolean isCurrenciesValid(String from, String to) {
        Optional<Currency> fromCurrency = currencyRepository.findByCode(from);
        Optional<Currency> toCurrency = currencyRepository.findByCode(to);

        return (fromCurrency.isPresent() && toCurrency.isPresent());
    }
    private Optional<ExchangeRate> findExchangeRate(String from, String to){
        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(from,to);

        if (exchangeRate.isPresent()){
            return Optional.of(exchangeRate.get());
        }

        exchangeRate = exchangeRatesRepository.findByCodes(to,from);

        if (exchangeRate.isPresent()){
            return Optional.of(exchangeRate.get());
        }

        BigDecimal rate = exchangeRatesRepository.findByCodes("USD", from).get().getRate()
                .divide(exchangeRatesRepository.findByCodes("USD",to).get().getRate());

        return Optional.ofNullable(ExchangeRate.builder()
                .baseCurrency(currencyRepository.findByCode(from).get())
                .targetCurrency(currencyRepository.findByCode(to).get())
                .rate(rate)
                .build());
    }

}
