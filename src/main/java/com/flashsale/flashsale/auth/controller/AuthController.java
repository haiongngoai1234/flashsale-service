package com.flashsale.flashsale.auth.controller;

import com.flashsale.flashsale.auth.request.AuthRequest;
import com.flashsale.flashsale.auth.request.VerifyOtpRequest;
import com.flashsale.flashsale.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> auth(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verify(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }
}