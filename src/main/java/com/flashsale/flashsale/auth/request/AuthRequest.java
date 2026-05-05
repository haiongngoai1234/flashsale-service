package com.flashsale.flashsale.auth.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String identifier;
    private String password;
}