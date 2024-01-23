package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(value = "/currencies")
@Slf4j
public class CurrenciesServlet extends HttpServlet {
    private CurrencyRepository currencyRepository;

    @Override
    public void init() {
        currencyRepository = new CurrencyRepository();


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        try {
            List<Currency> currencies = currencyRepository.findAll();

            // Конвертуємо список в JSON і виводимо у відповідь
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(currencies);

            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (Exception e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Internal Server Error 500");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (currencyRepository.findByCode(code).isPresent()) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency with this name already exist");
            return;
        }

        currencyRepository.save(Currency.builder().code(code).fullName(name).sign(sign).build());

        doGet(req, resp);
    }
}
