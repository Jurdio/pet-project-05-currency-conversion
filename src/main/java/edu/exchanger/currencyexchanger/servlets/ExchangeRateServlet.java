package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.repositories.ExchangeRatesRepository;
import edu.exchanger.currencyexchanger.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("PATCH"))
            doPatch(req, resp);
        else
            super.service(req, resp);
    }
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Отримання значення "rate" з тіла запиту
        String rate = extractRateFromRequestBody(request);

        if (rate == null || rate.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Отсутствует обменный курс");
            return;
        }

        if (!Util.isStringDouble(rate)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Введите значение. Пример: 1.05");
            return;
        }

        // Отримання параметрів з URL
        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(
                currenciesCodes.substring(0, 3), currenciesCodes.substring(3, 6));

        // Опрацювання результатів
        if (exchangeRate.isPresent()) {
            exchangeRate.get().setRate(BigDecimal.valueOf(Double.parseDouble(rate)));
            exchangeRatesRepository.update(exchangeRate.get());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Exchange rate updated successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Exchange rate not found");
        }
    }

    private String extractRateFromRequestBody(HttpServletRequest request) throws IOException {
        // Отримання тіла запиту
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Розбір параметрів
        String[] params = requestBody.toString().split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("rate")) {
                return keyValue[1];
            }
        }

        return null; // Якщо "rate" не знайдено в тілі запиту
    }
}
