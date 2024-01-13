package edu.exchanger.currencyexchanger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@JsonPropertyOrder({"id", "fullName", "code", "sign"})
public class Currency {
    private int id;
    private String fullName;
    private String code;
    private String sign;
}
