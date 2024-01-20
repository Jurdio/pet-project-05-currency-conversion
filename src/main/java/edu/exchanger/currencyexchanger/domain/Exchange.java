package edu.exchanger.currencyexchanger.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class Exchange {
    private ExchangeRate exchangeRate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
