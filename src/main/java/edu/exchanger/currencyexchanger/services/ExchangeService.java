package edu.exchanger.currencyexchanger.services;


import edu.exchanger.currencyexchanger.domain.Exchange;
import edu.exchanger.currencyexchanger.domain.ExchangeRate;

import edu.exchanger.currencyexchanger.repositories.CurrencyRepository;
import edu.exchanger.currencyexchanger.repositories.ExchangeRatesRepository;
;
import lombok.Getter;


import java.math.BigDecimal;
import java.util.Optional;

@Getter
public class ExchangeService {
    private final Optional<ExchangeRate> exchangeRate;
    private CurrencyRepository currencyRepository;
    private ExchangeRatesRepository exchangeRatesRepository;
    private BigDecimal amount;

    public ExchangeService(String from, String to, String amount){
        currencyRepository = new CurrencyRepository();
        exchangeRatesRepository = new ExchangeRatesRepository();
        exchangeRate = findExchangeRate(from,to);
        this.amount = BigDecimal.valueOf(Double.parseDouble(amount));
    }
    public boolean exchangeIsEmpty(){
        return exchangeRate.isEmpty();
    }

    public Exchange getExchange(){
        return Exchange.builder()
                .exchangeRate(exchangeRate.orElseThrow())
                .amount(amount)
                .convertedAmount(getConvertedAmount())
                .build();
    }
    private BigDecimal getConvertedAmount(){
        return exchangeRate.orElseThrow().getRate().multiply(amount);
    }
    private Optional<ExchangeRate> findExchangeRate(String from, String to){
        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(from,to);

        if (exchangeRate.isPresent()){
            return Optional.of(exchangeRate.get());
        }

        exchangeRate = exchangeRatesRepository.findByCodes(to,from);

        if (exchangeRate.isPresent()){
            return Optional.of(exchangeRate.get());
        }

        BigDecimal rate = exchangeRatesRepository.findByCodes("USD", from).get().getRate()
                .divide(exchangeRatesRepository.findByCodes("USD",to).get().getRate());

        return Optional.ofNullable(ExchangeRate.builder()
                .baseCurrency(currencyRepository.findByCode(from).get())
                .targetCurrency(currencyRepository.findByCode(to).get())
                .rate(rate)
                .build());
    }
}
