package com.cap.midsenior.prices_api.application.services;

import com.cap.midsenior.prices_api.domain.model.Price;
import com.cap.midsenior.prices_api.domain.ports.in.GetPriceUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Configuration
public class PriceService {

    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    /**
     * Find the highest priority price in a given date, brand and product
     *
     * @param date
     * @param productId
     * @param brandId
     * @return
     */
    public Optional<Price> findByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {
        return Optional.empty();
    }

}
