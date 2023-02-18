package com.market.market.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ResponseStatus {
    SUCCESS("success"),
    FAILED("failed");
    private final String status;

    ResponseStatus(String type) {
        this.status = type;
    }

    public String getStatus() {
        return status;
    }
}
