package com.flashsale.flashsale.auth.request;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String identifier;
    private String code;
}
