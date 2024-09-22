package com.cap.midsenior.prices_api.application.services;

import com.cap.midsenior.prices_api.domain.model.Price;
import com.cap.midsenior.prices_api.domain.ports.in.GetPriceUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private GetPriceUseCase getPriceUseCase;

    @InjectMocks
    private PriceService priceService;


    @Test
    public void findByDateProductAndBrand_Given_null_date_Then_return_error() {
        // Given:
        LocalDateTime date = null;
        Long productId = 1L;
        Long brandId = 1L;

        // When / Then
        IllegalArgumentException thrown =
                org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                        priceService.findByDateProductAndBrand(date, productId, brandId)
                );
        assertThat(thrown.getMessage()).isEqualTo("Date must not be null and must be a valid date");
    }

    @Test
    public void findByDateProductAndBrand_Given_null_product_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = null;
        Long brandId = 1L;

        // When / Then
        IllegalArgumentException thrown =
                org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                        priceService.findByDateProductAndBrand(date, productId, brandId)
                );
        assertThat(thrown.getMessage()).isEqualTo("ProductId must be non-null and positive");
    }

    @Test
    public void findByDateProductAndBrand_Given_negative_product_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = -1L;
        Long brandId = 1L;

        // When / Then
        IllegalArgumentException thrown =
                org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                        priceService.findByDateProductAndBrand(date, productId, brandId)
                );
        assertThat(thrown.getMessage()).isEqualTo("ProductId must be non-null and positive");
    }

    @Test
    public void findByDateProductAndBrand_Given_null_brand_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = null;

        // When / Then
        IllegalArgumentException thrown =
                org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                        priceService.findByDateProductAndBrand(date, productId, brandId)
                );
        assertThat(thrown.getMessage()).isEqualTo("BrandId must be non-null and positive");
    }

    @Test
    public void findByDateProductAndBrand_Given_negative_brand_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = -1L;

        // When / Then
        IllegalArgumentException thrown =
                org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
                        priceService.findByDateProductAndBrand(date, productId, brandId)
                );
        assertThat(thrown.getMessage()).isEqualTo("BrandId must be non-null and positive");
    }

    @Test
    void findByDateProductAndBrand_Given_empty_prices_Should_return_empty_optional() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        when(getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Collections.emptyList());

        // When
        Optional<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void findByDateProductAndBrand_Given_one_price_Should_return_it() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        Price price = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();

        when(getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Collections.singletonList(price));

        // When
        Optional<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result).contains(price);
    }

    @Test
    public void findByDateProductAndBrand_Given_several_matching_prices_Should_return_max_priority_one() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        Price price1 = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();

        Price price2 = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .priority(2) // max priority
                .priceAmount(BigDecimal.valueOf(30.00))
                .currency("EUR")
                .build();

        when(getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Arrays.asList(price1, price2));

        // When
        Optional<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result).contains(price2); // Should get max priority one
    }

}