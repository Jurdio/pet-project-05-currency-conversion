package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.zaxxer.hikari.HikariDataSource;
import edu.exchanger.currencyexchanger.models.Currency;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private Logger logger;

    @Override
    public void init() {
        currencyRepository = new CurrencyRepository();

        logger = LoggerFactory.getLogger(CurrenciesServlet.class);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            List<Currency> currencies = currencyRepository.findAll();

            // Логуємо кількість валют у списку
            logger.info("Number of currencies: {}", currencies.size());

            // Конвертуємо список в JSON і виводимо у відповідь
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currencies);

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
