package com.cap.midsenior.prices_api.application.services;

import com.cap.midsenior.prices_api.domain.model.Price;
import com.cap.midsenior.prices_api.domain.ports.in.GetPriceUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Configuration
public class PriceService {

    private final GetPriceUseCase getPriceUseCase;
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
        logger.info("Finding price for date: {}, productId: {}, brandId: {}", date, productId, brandId);

        // Check params
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null and must be a valid date");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("ProductId must be non-null and positive");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("BrandId must be non-null and positive");
        }

        // Find prices
        List<Price> prices = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Return the highest priority price or an empty Optional if no prices found
        return prices.stream()
                .reduce((price1, price2) ->
                        // delete priority decision to domain logic
                        price1.hasHigherPriorityThan(price2) ? price1 : price2
                );
    }

}
