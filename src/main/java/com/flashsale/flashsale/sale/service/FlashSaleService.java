package com.flashsale.flashsale.sale.service;

import com.flashsale.flashsale.sale.entity.FlashSale;
import com.flashsale.flashsale.sale.entity.FlashSaleItem;
import com.flashsale.flashsale.sale.entity.Order;
import com.flashsale.flashsale.sale.repository.FlashSaleItemRepository;
import com.flashsale.flashsale.sale.repository.FlashSaleRepository;
import com.flashsale.flashsale.sale.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashSaleService {

    private final FlashSaleItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final FlashSaleRepository flashSaleRepository;

    @Transactional
    public void buy(Long userId, Long itemId) {

        // NOTE:
        // - Using DB atomic update to prevent overselling under concurrent requests.
        // - Inventory is updated synchronously to ensure strong consistency.
        // - In high-scale systems, Redis or distributed locking can reduce DB contention.
        // - Event-driven architecture (Kafka/RabbitMQ) can be introduced to decouple order processing.
        int updated = itemRepository.decreaseStock(itemId);

        if (updated == 0) {
            throw new RuntimeException("Out of stock");
        }

        // NOTE: Unique constraint (user_id + date) is used to guarantee one purchase per user per day.
        // This avoids race conditions even under concurrent requests.
        // In production, idempotency key can be added to further prevent duplicate submissions.

        // 2. create order
        Order order = Order.builder()
                .userId(userId)
                .flashSaleItemId(itemId)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Already bought today");
        }
    }

    public List<FlashSaleItem> getCurrentFlashSaleItems() {

        FlashSale flashSale = flashSaleRepository
                .findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new RuntimeException("No active flash sale"));

        return itemRepository.findByFlashSaleId(flashSale.getId());
    }
}
