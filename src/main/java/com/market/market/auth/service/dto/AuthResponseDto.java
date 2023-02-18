package com.market.market.auth.service.dto;

import com.market.market.enums.ResponseStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private int total;
    private ResponseStatus status;
    private String errorMessage;
    private Integer errorCode;
    private List<?> data;
}
