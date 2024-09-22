package com.cap.midsenior.prices_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Price {

    private Long brandId;
    private Long productId;
    private Long priceListId;
    /**
     * Dates where the price is used
     */
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    /**
     * priority is used to define which Price is used, in case of date collision. Choose the higher one.
     */
    private int priority;
    private BigDecimal priceAmount;
    private String currency;

    /**
     * Compares this Price with another Price based on priority.
     *
     * @param other the other Price to compare with
     * @return Negative if this Price has less priority, zero if equal priority,
     * positive if this Price has greater priority than the other Price
     */
    public int comparePriority(Price other) {
        return Integer.compare(this.priority, other.getPriority());
    }

    /**
     * Determines if this Price has higher priority than another Price.
     *
     * @param other the other Price to compare with
     * @return true if this Price has higher priority, false otherwise
     */
    public boolean hasHigherPriorityThan(Price other) {
        return this.comparePriority(other) > 0;
    }

}