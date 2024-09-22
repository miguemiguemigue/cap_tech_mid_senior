package com.cap.midsenior.prices_api.adapter.in.rest.controller;

import com.cap.midsenior.prices_api.adapter.in.rest.dto.PriceResponse;
import com.cap.midsenior.prices_api.adapter.in.rest.mapper.PriceResponseMapper;
import com.cap.midsenior.prices_api.application.services.PriceService;
import com.cap.midsenior.prices_api.domain.model.Price;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class PricesApiController implements PricesApi {

    private final PriceService priceService;
    private static final Logger logger = LoggerFactory.getLogger(PricesApiController.class);

    @Override
    public ResponseEntity<PriceResponse> pricesGet(LocalDateTime date, Long productId, Long brandId) {

        Optional<Price> price = priceService.findByDateProductAndBrand(date, productId, brandId);

        if (price.isEmpty()) {
            logger.warn("No prices found for productId: {}, brandId: {}", productId, brandId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        PriceResponse priceResponse = PriceResponseMapper.fromDomain(price.get());
        logger.info("Retrieved price for productId: {}, brandId: {}", productId, brandId);

        return ResponseEntity.ok(priceResponse);
    }
}
