package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
@MultipartConfig
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
            out.println("Internal Server Error 500");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

//        if (Util.isNotValidCurrenciesArgs(code, name, sign)){
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect input. Example: code = 'USD', name = 'US Dollar', sign = '$')");
//        }

        if (currencyRepository.findByCode(code).isPresent()) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency with this name already exist");
            return;
        }

        currencyRepository.save(Currency.builder().code(code).fullName(name).sign(sign).build());

        doGet(req, resp);
    }
}
