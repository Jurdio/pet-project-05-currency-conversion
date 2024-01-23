package edu.exchanger.currencyexchanger.mappers;

import edu.exchanger.currencyexchanger.domain.Currency;
import edu.exchanger.currencyexchanger.dto.CurrencyDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CurrencyMapper {
    Currency toCurrency(CurrencyDTO currencyDTO);
    CurrencyDTO toCurrencyDTO(Currency currency);
}
