package edu.exchanger.currencyexchanger.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exchanger.currencyexchanger.dto.ExchangeDTO;
import edu.exchanger.currencyexchanger.mappers.ExchangeMapper;
import edu.exchanger.currencyexchanger.services.ExchangeService;
import edu.exchanger.currencyexchanger.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (Util.isNotValidExchangeArgs(from, to, amount) || !Util.isStringDouble(amount)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input invalid request. Пример: /exchange?from=USD&to=UAH&amount=10");
            return;
        }

        ExchangeService exchangeService = new ExchangeService(from, to, amount);

        if (exchangeService.exchangeIsEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "ExchangeRate or Currency does not exist. Пример: /exchange?from=USD&to=UAH&amount=10");
            return;
        }

        ExchangeDTO exchangeDTO = ExchangeMapper.INSTANCE.toExchangeDTO(exchangeService.getExchange());

        new ObjectMapper().writeValue(resp.getWriter(), exchangeDTO);
    }
}
