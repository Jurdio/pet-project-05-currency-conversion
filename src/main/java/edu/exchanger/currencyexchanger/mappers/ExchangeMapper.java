package edu.exchanger.currencyexchanger.mappers;

import edu.exchanger.currencyexchanger.domain.Exchange;

import edu.exchanger.currencyexchanger.dto.ExchangeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeMapper {
    @Mapping(source = "exchange.exchangeRate.baseCurrency", target = "baseCurrency")
    @Mapping(source = "exchange.exchangeRate.targetCurrency", target = "targetCurrency")
    @Mapping(source = "exchange.exchangeRate.rate", target = "rate")
    public ExchangeDTO toExchangeDTO (Exchange exchange);
    public Exchange toExchange (ExchangeDTO exchangeDTO);

}
