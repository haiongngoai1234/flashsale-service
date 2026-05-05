package com.flashsale.flashsale.sale.controller;

import com.flashsale.flashsale.sale.common.ApiResponse;
import com.flashsale.flashsale.sale.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flash-sale")
@RequiredArgsConstructor
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    @PostMapping("/buy")
    public ResponseEntity<ApiResponse<Object>> buy(
            @RequestParam Long userId,
            @RequestParam Long itemId
    ) {
        flashSaleService.buy(userId, itemId);
        return ResponseEntity.ok(new ApiResponse<>("Success"));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentFlashSale() {
        return ResponseEntity.ok(flashSaleService.getCurrentFlashSaleItems());
    }
}
