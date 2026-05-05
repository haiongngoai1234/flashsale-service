package com.flashsale.flashsale.auth.entity;

import com.flashsale.flashsale.auth.enumtype.OtpType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes", indexes = {
        @Index(name = "idx_user_code", columnList = "userId, code, isUsed")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String code;

    @Enumerated(EnumType.STRING)
    private OtpType type;

    private LocalDateTime expiredAt;

    private Boolean isUsed = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
