package com.flashsale.flashsale.auth.repository;

import com.flashsale.flashsale.auth.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByUserIdAndCodeAndIsUsedFalseOrderByCreatedAtDesc(Long id, String code);

    // 1 user can have many otp invalid
    @Modifying
    @Query("update OtpCode o set o.isUsed = true where o.userId = :userId and o.isUsed = false")
    void invalidateAllByUserId(Long userId);

    Optional<OtpCode> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
