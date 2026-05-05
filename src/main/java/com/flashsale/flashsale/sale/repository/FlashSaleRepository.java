package com.flashsale.flashsale.sale.repository;

import com.flashsale.flashsale.sale.entity.FlashSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Long> {

    Optional<FlashSale> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            LocalDateTime now1,
            LocalDateTime now2
    );
}
