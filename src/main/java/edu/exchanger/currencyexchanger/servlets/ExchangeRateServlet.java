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
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Коды валют пары отсутствуют в адресе. Пример: .../exchangeRate/USDUAH");
        }

        String currenciesCodes = req.getPathInfo().replaceFirst("/","").toUpperCase();

        if (currenciesCodes.length() != 6){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Указана не корректная пара валют. Пример: .../exchangeRate/USDUAH");
        }

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(currenciesCodes.substring(0,3), currenciesCodes.substring(3,6));

        if (!exchangeRate.isPresent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Обменный курс для пары не найден");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(exchangeRate.get());

        resp.setStatus(HttpServletResponse.SC_OK);
        out.println(json);
    }
}
