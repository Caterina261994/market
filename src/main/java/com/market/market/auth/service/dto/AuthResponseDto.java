package com.market.market.auth.service.dto;

import com.market.market.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private int total;
    private ResponseStatus status;
    private String errorMessage;
    private String errorCode;
    private List<?> data;
}
