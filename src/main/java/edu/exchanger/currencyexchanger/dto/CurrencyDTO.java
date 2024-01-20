package edu.exchanger.currencyexchanger.dto;

import lombok.NonNull;

public class CurrencyDTO {
    private int id;
    @NonNull
    private String code;
    @NonNull
    private String fullName;
    @NonNull
    private String sign;
}
