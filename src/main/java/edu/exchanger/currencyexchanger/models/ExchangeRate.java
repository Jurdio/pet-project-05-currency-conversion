package edu.exchanger.currencyexchanger.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

}
