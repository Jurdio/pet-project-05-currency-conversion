package edu.exchanger.currencyexchanger.mappers;

import edu.exchanger.currencyexchanger.domain.Exchange;

import edu.exchanger.currencyexchanger.dto.ExchangeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExchangeMapper {
    public ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);
    default ExchangeDTO toExchangeDTO (Exchange exchange){
        ExchangeDTO exchangeDTO = new ExchangeDTO();

        exchangeDTO.setBaseCurrency(exchange.getExchangeRate().getBaseCurrency());
        exchangeDTO.setTargetCurrency(exchange.getExchangeRate().getTargetCurrency());
        exchangeDTO.setRate(exchange.getExchangeRate().getRate());
        exchangeDTO.setAmount(exchange.getAmount());
        exchangeDTO.setConvertedAmount(exchange.getConvertedAmount());

        return exchangeDTO;
    }
    public Exchange toExchange (ExchangeDTO exchangeDTO);

}
