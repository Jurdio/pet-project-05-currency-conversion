package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.models.Currency;
import edu.exchanger.currencyexchanger.models.ExchangeRate;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import edu.exchanger.currencyexchanger.repositories.ExchangeRatesRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRatesRepository exchangeRatesRepository;

    private Logger logger;

    @Override
    public void init() {
        exchangeRatesRepository = new ExchangeRatesRepository();
        logger = LoggerFactory.getLogger(ExchangeRatesServlet.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            List<ExchangeRate> exchangeRates = exchangeRatesRepository.findAll();


            logger.info("Number of exchange rates: " + exchangeRates.size());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(exchangeRates);

            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (Exception e) {
            // Логуємо помилку
            logger.error("Error processing request", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Internal Server Error");
        }
    }
}
