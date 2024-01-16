package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import edu.exchanger.currencyexchanger.repositories.ExchangeRatesRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private ExchangeRatesRepository exchangeRatesRepository;

    private Logger logger;

    @Override
    public void init() {
        currencyRepository = new CurrencyRepository();
        exchangeRatesRepository = new ExchangeRatesRepository();
        logger = LoggerFactory.getLogger(ExchangeRatesServlet.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        try {
            List<ExchangeRate> exchangeRates = exchangeRatesRepository.findAll();


            logger.info("Number of exchange rates: " + exchangeRates.size());

            new ObjectMapper().writeValue(resp.getWriter(), exchangeRatesRepository.findAll());

            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            // Логуємо помилку
            logger.error("Error processing request", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            out.println("Internal Server Error 500");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        Optional<Currency> baseCurrency = currencyRepository.findByCode(baseCurrencyCode);
        Optional<Currency> targetCurrency = currencyRepository.findByCode(targetCurrencyCode);

        if (!baseCurrency.isPresent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Главная Валюта не найдена");
            return;
        }
        if (!targetCurrency.isPresent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, " целевая Валюта не найдена");
            return;
        }

        if (exchangeRatesRepository.findByCodes(baseCurrencyCode,targetCurrencyCode).isPresent()) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency rates already exist");
            return;
        }

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(),targetCurrency.get(),BigDecimal.valueOf(Double.parseDouble(rate)));

        exchangeRatesRepository.save(exchangeRate);
        doGet(req, resp);
    }
}
