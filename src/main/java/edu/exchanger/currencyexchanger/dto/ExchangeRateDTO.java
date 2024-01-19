package edu.exchanger.currencyexchanger.dto;

import edu.exchanger.currencyexchanger.domain.Currency;
import lombok.NonNull;

import java.math.BigDecimal;

public class ExchangeRateDTO {
        private int id;
        @NonNull
        private Currency baseCurrency;
        @NonNull
        private Currency targetCurrency;
        @NonNull
        private BigDecimal rate;


}
