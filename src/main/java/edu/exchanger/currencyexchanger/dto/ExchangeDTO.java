package edu.exchanger.currencyexchanger.dto;

import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;

import java.math.BigDecimal;

public class ExchangeDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
