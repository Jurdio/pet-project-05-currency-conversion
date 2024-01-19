package edu.exchanger.currencyexchanger.services;

import edu.exchanger.currencyexchanger.domain.Exchange;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
@AllArgsConstructor
@Builder
public class ExchangeService {
    @NonNull
    private final ExchangeRate exchangeRate;
    @NonNull
    private final BigDecimal amount;
    private BigDecimal convertedAmount;

    public Exchange getExchange(){
        return Exchange.builder()
                .exchangeRate(exchangeRate)
                .amount(amount)
                .convertedAmount(getConvertedAmount()).build();
    }
    private BigDecimal getConvertedAmount(){
        return exchangeRate.getRate().multiply(amount);
    }
}
