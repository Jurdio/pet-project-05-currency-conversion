package edu.exchanger.currencyexchanger.dto;


import edu.exchanger.currencyexchanger.domain.Currency;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeDTO {
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
