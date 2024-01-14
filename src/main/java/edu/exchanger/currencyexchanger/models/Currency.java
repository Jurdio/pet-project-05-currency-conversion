package edu.exchanger.currencyexchanger.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"id", "fullName", "code", "sign"})
public class Currency {
    private int id;
    @NonNull
    private String code;
    @NonNull
    private String fullName;
    @NonNull
    private String sign;
}
