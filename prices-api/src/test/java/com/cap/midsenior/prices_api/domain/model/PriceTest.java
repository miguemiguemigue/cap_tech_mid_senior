package com.cap.midsenior.prices_api.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PriceTest {

    @Test
    public void comparePriority_Given_different_priorities_Should_detect_higher_one() {
        // Given:
        Price pricePriority1 = Price.builder()
                .priority(1)
                .build();
        Price pricePriority2 = Price.builder()
                .priority(2)
                .build();

        // Then:
        // Testing that pricePriority2 has higher priority than pricePriority1
        assertThat(pricePriority1.comparePriority(pricePriority2)).isLessThan(0);
        assertThat(pricePriority2.comparePriority(pricePriority1)).isGreaterThan(0);
        assertThat(pricePriority1.comparePriority(pricePriority1)).isEqualTo(0);
    }

    @Test
    public void hasHigherPriorityThan_Given_different_priorities_Should_detect_higher_one() {
        // Given:
        Price price1 = Price.builder()
                .priority(1)
                .build();
        Price price2 = Price.builder()
                .priority(2)
                .build();

        // Then:
        // Testing that price2 has higher priority than price1
        assertThat(price1.hasHigherPriorityThan(price2)).isFalse();
        assertThat(price2.hasHigherPriorityThan(price1)).isTrue();
    }
}