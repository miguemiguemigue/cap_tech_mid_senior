package com.cap.midsenior.prices_api.adapter.out.database.h2.adapter;

import com.cap.midsenior.prices_api.adapter.out.database.h2.entity.PriceEntity;
import com.cap.midsenior.prices_api.adapter.out.database.h2.repository.PriceRepository;
import com.cap.midsenior.prices_api.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PriceRepositoryAdapterTest {

    @Autowired
    private PriceRepository priceRepository;

    private static PriceRepositoryAdapter priceRepositoryAdapter;

    @BeforeEach
    void setUp() {
        priceRepositoryAdapter = new PriceRepositoryAdapter(priceRepository);
        // clean database
        priceRepository.deleteAll();
    }


    /**
     * This test creates several prices that match by valid date, but only one that match by date and ids.
     * Should find the only one that matches all conditions.
     */
    @Test
    void getPricesByDateProductAndBrand_Given_several_prices_matching_by_date_and_only_one_matching_date_product_and_brand_id_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime of = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);

        // Persist prices to h2
        priceRepository.saveAll(List.of(
                // should find this one
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .priceListId(1L)
                        .startDate(of)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(testPrice)
                        .priority(1)
                        .build(),
                // valid date, but different product and brand ids
                PriceEntity.builder()
                        .productId(testProductId + 1L)
                        .brandId(testBrandId + 1L)
                        .priceListId(1L)
                        .startDate(LocalDateTime.of(2024, 9, 19, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(BigDecimal.valueOf(19.99))
                        .priority(2)
                        .build(),
                PriceEntity.builder()
                        .productId(testProductId + 2L)
                        .brandId(testBrandId + 2L)
                        .priceListId(1L)
                        .startDate(LocalDateTime.of(2024, 9, 17, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(BigDecimal.valueOf(39.99))
                        .priority(1)
                        .build()
        ));

        LocalDateTime testDate = of.plusDays(1);

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        assertThat(result).hasSize(1);
        Price foundPrice = result.get(0);
        assertThat(foundPrice.getPriceAmount()).isEqualTo(testPrice);
        assertThat(foundPrice.getBrandId()).isEqualTo(testBrandId);
        assertThat(foundPrice.getProductId()).isEqualTo(testProductId);
    }

    /**
     * This test creates several prices that match by product id and valid date, but only one that match by date and ids.
     * Should find the only one that matches all conditions.
     */
    @Test
    void getPricesByDateProductAndBrand_Given_several_prices_matching_by_date_and_product_but_only_one_match_all_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime of = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);

        // Persist
        priceRepository.saveAll(List.of(
                // should find this one
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(of)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(testPrice)
                        .priority(1)
                        .build(),
                // same product and valid dates, but different brand id
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId + 1L)
                        .startDate(LocalDateTime.of(2024, 9, 19, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(BigDecimal.valueOf(19.99))
                        .priority(2)
                        .build(),
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId + 2L)
                        .startDate(LocalDateTime.of(2024, 9, 17, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(BigDecimal.valueOf(39.99))
                        .priority(1)
                        .build()
        ));

        LocalDateTime testDate = of.plusDays(1);

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        assertThat(result).hasSize(1);
        Price foundPrice = result.get(0);
        assertThat(foundPrice.getPriceAmount()).isEqualTo(testPrice);
        assertThat(foundPrice.getBrandId()).isEqualTo(testBrandId);
        assertThat(foundPrice.getProductId()).isEqualTo(testProductId);
    }

    /**
     * This test creates several prices that match by brand id and date, but only one that match by all three conditions.
     * Should find the only one that matches all conditions
     */
    @Test
    void getPricesByDateProductAndBrand_Given_several_prices_matching_by_date_and_brand_but_only_one_match_all_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime of = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);

        // Persist
        priceRepository.saveAll(List.of(
                // same brand and valid dates, but different product id
                PriceEntity.builder()
                        .productId(testProductId + 1L)
                        .brandId(testBrandId)
                        .startDate(of)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(BigDecimal.valueOf(19.99))
                        .priority(1)
                        .build(),
                PriceEntity.builder()
                        .productId(testProductId + 2L)
                        .brandId(testBrandId)
                        .startDate(LocalDateTime.of(2024, 9, 19, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(BigDecimal.valueOf(19.99))
                        .priority(2)
                        .build(),
                // should find this one
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(LocalDateTime.of(2024, 9, 17, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(testPrice)
                        .priority(1)
                        .build()
        ));

        LocalDateTime testDate = of.plusDays(1);

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        assertThat(result).hasSize(1);
        Price foundPrice = result.get(0);
        assertThat(foundPrice.getPriceAmount()).isEqualTo(testPrice);
        assertThat(foundPrice.getBrandId()).isEqualTo(testBrandId);
        assertThat(foundPrice.getProductId()).isEqualTo(testProductId);

    }

    /**
     * This test creates one price which start date is the same of the search date
     * Should find it
     */
    @Test
    void getPricesByDateProductAndBrand_Given_start_date_equals_search_date_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime testDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);

        // Persist
        priceRepository.save(
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(testDate)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(testPrice)
                        .priority(1)
                        .build()
        );

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        assertThat(result).hasSize(1);
        Price foundPrice = result.get(0);
        assertThat(foundPrice.getPriceAmount()).isEqualTo(testPrice);
        assertThat(foundPrice.getBrandId()).isEqualTo(testBrandId);
        assertThat(foundPrice.getProductId()).isEqualTo(testProductId);
    }

    /**
     * This test creates one price which start date greater than the search date
     * Should not find it
     */
    @Test
    void getPricesByDateProductAndBrand_Given_start_date_greater_than_search_date_Should_not_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(19.99);

        // Persist
        priceRepository.save(
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(startDate)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .priceAmount(testPrice)
                        .priority(1)
                        .build()
        );

        LocalDateTime testDate = startDate.minusSeconds(1);

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        assertThat(result).isEmpty();
    }

    /**
     * This test creates one price where the end date is equal to the search date.
     * It should find the price.
     * Note: Prices with an end date equal to the search date should not be discarded. This is business logic
     * and shouldn't be handled here.
     */
    @Test
    void getPricesByDateProductAndBrand_Given_end_date_equal_than_search_date_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(19.99);
        LocalDateTime endDateTest = LocalDateTime.of(2024, 9, 22, 0, 0);

        // Save the price entity
        priceRepository.save(
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(startDate)
                        .endDate(endDateTest)
                        .priceAmount(testPrice)
                        .priority(1)
                        .build()
        );

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(endDateTest, testProductId, testBrandId);

        // Then:
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriceAmount()).isEqualTo(testPrice);
        assertThat(result.get(0).getBrandId()).isEqualTo(testBrandId);
        assertThat(result.get(0).getProductId()).isEqualTo(testProductId);
    }

    /**
     * This test creates one price where the end date is less than the search date.
     * It should not find the price.
     */
    @Test
    void getPricesByDateProductAndBrand_Given_end_date_less_than_search_date_Should_not_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(19.99);
        LocalDateTime endDateTest = LocalDateTime.of(2024, 9, 22, 0, 0);  // End date is less than search date

        // Persist
        priceRepository.save(
                PriceEntity.builder()
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(startDate)
                        .endDate(endDateTest)
                        .priceAmount(testPrice)
                        .priority(1)
                        .build()
        );

        // The search date is later than the end date
        LocalDateTime testDate = endDateTest.plusSeconds(1);

        // When:
        List<Price> result = priceRepositoryAdapter.getPriceByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        assertThat(result).isEmpty();
    }


}