package com.flashsale.flashsale.sale.repository;

import com.flashsale.flashsale.sale.entity.FlashSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlashSaleItemRepository extends JpaRepository<FlashSaleItem, Long> {

    // atomic update , for oversell
    @Modifying
    @Query("""
        UPDATE FlashSaleItem f
        SET f.stock = f.stock - 1
        WHERE f.id = :id AND f.stock > 0
    """)
    int decreaseStock(Long id);

    List<FlashSaleItem> findByFlashSaleId(Long flashSaleId);

    // rollback stock if fail
    @Modifying
    @Query("""
        UPDATE FlashSaleItem f
        SET f.stock = f.stock + 1
        WHERE f.id = :itemId
    """)
    void increaseStock(Long itemId);
}
