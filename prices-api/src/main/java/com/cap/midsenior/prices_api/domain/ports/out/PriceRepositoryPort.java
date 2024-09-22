package com.cap.midsenior.prices_api.domain.ports.out;

import com.cap.midsenior.prices_api.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    /**
     * Get prices that match the following conditions:
     * - Price start date is less than or equal to search date
     * - Price end date is greater than or equal to search date
     * - Price product id matches
     * - Price brand id matches
     *
     * @param date      the date to search for prices
     * @param productId the product ID to match
     * @param brandId   the brand ID to match
     * @return a list of prices matching the criteria
     */
    List<Price> getPriceByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId);

}