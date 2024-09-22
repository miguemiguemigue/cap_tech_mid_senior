package com.cap.midsenior.prices_api.application.usecases;

import com.cap.midsenior.prices_api.domain.model.Price;
import com.cap.midsenior.prices_api.domain.ports.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPriceUseCaseImplTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private GetPriceUseCaseImpl getPriceUseCase;

    @Test
    public void getPriceByDateProductAndBrand_Given_repository_return_one_price_Then_return_it() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;
        Price price = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .build();

        when(priceRepositoryPort.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(List.of(price));

        // When
        List<Price> result = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result).containsExactly(price);
        verify(priceRepositoryPort).getPriceByDateProductAndBrand(date, productId, brandId);
    }

    @Test
    public void getPriceByDateProductAndBrand_Given_repository_return_no_prices_Then_return_empty() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        when(priceRepositoryPort.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Collections.emptyList());

        // When
        List<Price> result = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result).isEmpty();
        verify(priceRepositoryPort).getPriceByDateProductAndBrand(date, productId, brandId);
    }

    @Test
    public void getPriceByDateProductAndBrand_Given_repository_return_multiple_prices_Then_return_all_prices() {
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
                .priority(2)
                .priceAmount(BigDecimal.valueOf(30.00))
                .currency("EUR")
                .build();

        when(priceRepositoryPort.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(List.of(price1, price2));

        // When
        List<Price> result = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result).containsExactly(price1, price2);
        verify(priceRepositoryPort).getPriceByDateProductAndBrand(date, productId, brandId);
    }

}