package com.flashsale.flashsale.auth.service;

import com.flashsale.flashsale.auth.entity.OtpCode;
import com.flashsale.flashsale.auth.entity.User;
import com.flashsale.flashsale.auth.enumtype.OtpType;
import com.flashsale.flashsale.auth.repository.OtpCodeRepository;
import com.flashsale.flashsale.auth.repository.UserRepository;
import com.flashsale.flashsale.auth.request.AuthRequest;
import com.flashsale.flashsale.auth.request.VerifyOtpRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final OtpCodeRepository otpRepository;
    // can create bean after
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String auth(AuthRequest request) {

        String identifier = request.getIdentifier();
        String password = request.getPassword();

        Optional<User> userOpt = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByPhone(identifier);

        if (userOpt.isEmpty()) {
            // REGISTER
            User user = new User();

            if (identifier.contains("@")) {
                user.setEmail(identifier);
            } else {
                user.setPhone(identifier);
            }

            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            generateOtp(user.getId(), identifier);

            return "User created, please verify OTP";
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsVerified()) {
            generateOtp(user.getId(), identifier);
            return "Please verify OTP";
        }

        // return token after
        return "Login success";
    }

    // NOTE: OTP is currently generated and stored directly.
    // In production, should integrate with external service (SMS/Email) and apply rate limiting.
    private void generateOtp(Long userId, String identifier) {

        // rate limit otp
        Optional<OtpCode> latestOtp = otpRepository
                .findTopByUserIdOrderByCreatedAtDesc(userId);

        if (latestOtp.isPresent() &&
                latestOtp.get().getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(30))) {
            throw new RuntimeException("Too many requests");
        }

        // validate old otp
        otpRepository.invalidateAllByUserId(userId);

        String code = String.valueOf((int)(Math.random() * 900000) + 100000);

        OtpCode otp = OtpCode.builder()
                .userId(userId)
                .code(code)
                .type(OtpType.valueOf(identifier.contains("@") ? "EMAIL" : "PHONE"))
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .isUsed(false)
                .build();

        otpRepository.save(otp);

        System.out.println("OTP: " + code);
    }

    // NOTE: OTP validation is basic.
    // Can be extended with retry limits, lockout mechanism, and anti-brute-force protection.
    public String verifyOtp(VerifyOtpRequest request) {

        String identifier = request.getIdentifier();
        String code = request.getCode();

        Optional<User> userOpt = identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                : userRepository.findByPhone(identifier);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = userOpt.get();

        Optional<OtpCode> otpOpt = otpRepository
                .findTopByUserIdAndCodeAndIsUsedFalseOrderByCreatedAtDesc(user.getId(), code);

        if (otpOpt.isPresent() && otpOpt.get().getExpiredAt().isBefore(LocalDateTime.now())) {
            otpOpt = Optional.empty();
        }
        if (otpOpt.isEmpty()) {
            throw new RuntimeException("Invalid OTP");
        }

        OtpCode otp = otpOpt.get();
        otp.setIsUsed(true);
        otpRepository.save(otp);

        user.setIsVerified(true);
        userRepository.save(user);

        return "Verify success";
    }
}
