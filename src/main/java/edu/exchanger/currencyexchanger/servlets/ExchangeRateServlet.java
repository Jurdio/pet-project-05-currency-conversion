package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.models.ExchangeRate;
import edu.exchanger.currencyexchanger.repositories.ExchangeRatesRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRatesRepository exchangeRatesRepository;

    @Override
    public void init() throws ServletException {
        exchangeRatesRepository = new ExchangeRatesRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No pair of currency codes in URL 400. Example: .../exchangeRate/USDUAH");
            }

            String currenciesCodes = req.getPathInfo().replaceFirst("/","").toUpperCase();

            if (currenciesCodes.length() != 6){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input incorrect pair of currencies 400. Example: .../exchangeRate/USDUAH");
            }

            Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(currenciesCodes.substring(0,3), currenciesCodes.substring(3,6));

            if (!exchangeRate.isPresent()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate for pair of currencies not found 404");
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(exchangeRate.get());

            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (Exception e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Internal Server Error 500");
        }

    }
}
