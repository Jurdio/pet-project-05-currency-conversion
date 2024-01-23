package edu.exchanger.currencyexchanger.dto;

import edu.exchanger.currencyexchanger.domain.Currency;
import lombok.NonNull;

import java.math.BigDecimal;

public class ExchangeRateDTO {
        private int id;
        private Currency baseCurrency;
        private Currency targetCurrency;
        private BigDecimal rate;
}
