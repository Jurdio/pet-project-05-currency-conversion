package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;
    private Logger logger;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyRepository = new CurrencyRepository();
        logger = LoggerFactory.getLogger(CurrencyServlet.class);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input incorrect currency 400. Example: .../currency/USD");
                return;
            }

            String currencyCode = request.getPathInfo().replaceFirst("/", "").toUpperCase();

            Optional<Currency> currency = currencyRepository.findByCode(currencyCode);

            if (!currency.isPresent()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency does not found 404. Example: .../currency/USD");
                return;
            }

            // Конвертуємо об'єкт Currency в JSON рядок
            String jsonCurrency = convertCurrencyToJson(currency.get());
            response.setStatus(HttpServletResponse.SC_OK);

            // Надсилаємо JSON як відповідь
            response.setContentType("application/json");
            response.getWriter().write(jsonCurrency);
        } catch (Exception e) {
            // Логуємо помилку
            logger.error("Error processing request", e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error 500");
        }

    }

    private String convertCurrencyToJson(Currency currency) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(currency);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}

