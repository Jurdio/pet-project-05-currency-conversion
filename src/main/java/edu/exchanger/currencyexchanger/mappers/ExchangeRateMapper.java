package edu.exchanger.currencyexchanger.mappers;

import edu.exchanger.currencyexchanger.domain.ExchangeRate;
import edu.exchanger.currencyexchanger.dto.ExchangeRateDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ExchangeRateMapper {
    ExchangeRate toExchangeRate(ExchangeRateDTO exchangeRateDTO);
    ExchangeRateDTO toExchangeRateDT(ExchangeRate exchangeRate);
}
