package com.cap.midsenior.prices_api.adapter.in.rest.controller;

import com.cap.midsenior.prices_api.adapter.in.rest.dto.PriceResponse;
import com.cap.midsenior.prices_api.adapter.out.database.h2.entity.PriceEntity;
import com.cap.midsenior.prices_api.adapter.out.database.h2.repository.PriceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PricesApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clean the database between tests
        priceRepository.deleteAll();
    }

    @Test
    void getPrices_Given_no_data_Then_return_none() {
        // Given:
        LocalDateTime date = LocalDateTime.parse("2018-06-14T12:00:00");
        Long productId = 1L;
        Long brandId = 1L;

        // When:
        var responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                String.class);

        // Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Request Test 1
     * "Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)"
     * Should find price 1
     */
    @Test
    void getPrices_Requested_Test_1(){
        //Given:
        LocalDateTime date = LocalDateTime.parse("2020-06-14T10:00:00");
        long productId = 35455L;
        long brandId = 1L;

        // With these parameters, should find sample price 1
        PriceEntity expectedPrice = buildSamplePrice1();
        // save sample prices
        priceRepository.save(expectedPrice);
        priceRepository.save(buildSamplePrice2());
        priceRepository.save(buildSamplePrice3());
        priceRepository.save(buildSamplePrice4());


        // When:
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(
                "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                PriceResponse.class);

        // Then:
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PriceResponse priceResponse = response.getBody();

        assertNotNull(priceResponse);
        assertThat(priceResponse.getBrandId()).isEqualTo(expectedPrice.getBrandId());
        assertThat(priceResponse.getStartDate()).isEqualTo(expectedPrice.getStartDate());
        assertThat(priceResponse.getEndDate()).isEqualTo(expectedPrice.getEndDate());
        assertThat(priceResponse.getPriceList()).isEqualTo(expectedPrice.getPriceListId());
        assertThat(priceResponse.getProductId()).isEqualTo(expectedPrice.getProductId());
        assertThat(priceResponse.getPrice()).isEqualTo(expectedPrice.getPriceAmount().floatValue());
        assertThat(priceResponse.getCurr()).isEqualTo(expectedPrice.getCurrency());
    }

    /**
     * Request Test 2
     * "Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)"
     * Should find price 2 because of priority
     */
    @Test
    void getPrices_Requested_Test_2(){
        //Given:
        LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        long productId = 35455L;
        long brandId = 1L;

        // With these parameters, should find sample price 2. Both price1 and price2 are active, but price 2 has top
        // priority
        PriceEntity expectedPrice = buildSamplePrice2();
        // save sample prices
        priceRepository.save(buildSamplePrice1());
        priceRepository.save(expectedPrice);
        priceRepository.save(buildSamplePrice3());
        priceRepository.save(buildSamplePrice4());


        // When:
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(
                "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                PriceResponse.class);

        // Then:
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PriceResponse priceResponse = response.getBody();

        assertNotNull(priceResponse);
        assertThat(priceResponse.getBrandId()).isEqualTo(expectedPrice.getBrandId());
        assertThat(priceResponse.getStartDate()).isEqualTo(expectedPrice.getStartDate());
        assertThat(priceResponse.getEndDate()).isEqualTo(expectedPrice.getEndDate());
        assertThat(priceResponse.getPriceList()).isEqualTo(expectedPrice.getPriceListId());
        assertThat(priceResponse.getProductId()).isEqualTo(expectedPrice.getProductId());
        assertThat(priceResponse.getPrice()).isEqualTo(expectedPrice.getPriceAmount().floatValue());
        assertThat(priceResponse.getCurr()).isEqualTo(expectedPrice.getCurrency());
    }

    /**
     * Request Test 3
     * "Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)"
     * Should find price 1 again, because price 2 is no longer overlapping
     */
    @Test
    void getPrices_Requested_Test_3(){
        //Given:
        LocalDateTime date = LocalDateTime.parse("2020-06-14T21:00:00");
        long productId = 35455L;
        long brandId = 1L;

        // With these parameters, should find sample price 1
        PriceEntity expectedPrice = buildSamplePrice1();
        // save sample prices
        priceRepository.save(expectedPrice);
        priceRepository.save(buildSamplePrice2());
        priceRepository.save(buildSamplePrice3());
        priceRepository.save(buildSamplePrice4());


        // When:
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(
                "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                PriceResponse.class);

        // Then:
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PriceResponse priceResponse = response.getBody();

        assertNotNull(priceResponse);
        assertThat(priceResponse.getBrandId()).isEqualTo(expectedPrice.getBrandId());
        assertThat(priceResponse.getStartDate()).isEqualTo(expectedPrice.getStartDate());
        assertThat(priceResponse.getEndDate()).isEqualTo(expectedPrice.getEndDate());
        assertThat(priceResponse.getPriceList()).isEqualTo(expectedPrice.getPriceListId());
        assertThat(priceResponse.getProductId()).isEqualTo(expectedPrice.getProductId());
        assertThat(priceResponse.getPrice()).isEqualTo(expectedPrice.getPriceAmount().floatValue());
        assertThat(priceResponse.getCurr()).isEqualTo(expectedPrice.getCurrency());
    }

    /**
     * Request Test 4
     * "Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)"
     * Should find price 3
     */
    @Test
    void getPrices_Requested_Test_4(){
        //Given:
        LocalDateTime date = LocalDateTime.parse("2020-06-15T10:00:00");
        long productId = 35455L;
        long brandId = 1L;

        // With these parameters, should find sample price 3
        PriceEntity expectedPrice = buildSamplePrice3();
        // save sample prices
        priceRepository.save(buildSamplePrice1());
        priceRepository.save(buildSamplePrice2());
        priceRepository.save(expectedPrice);
        priceRepository.save(buildSamplePrice4());


        // When:
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(
                "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                PriceResponse.class);

        // Then:
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PriceResponse priceResponse = response.getBody();

        assertNotNull(priceResponse);
        assertThat(priceResponse.getBrandId()).isEqualTo(expectedPrice.getBrandId());
        assertThat(priceResponse.getStartDate()).isEqualTo(expectedPrice.getStartDate());
        assertThat(priceResponse.getEndDate()).isEqualTo(expectedPrice.getEndDate());
        assertThat(priceResponse.getPriceList()).isEqualTo(expectedPrice.getPriceListId());
        assertThat(priceResponse.getProductId()).isEqualTo(expectedPrice.getProductId());
        assertThat(priceResponse.getPrice()).isEqualTo(expectedPrice.getPriceAmount().floatValue());
        assertThat(priceResponse.getCurr()).isEqualTo(expectedPrice.getCurrency());
    }

    /**
     * Request Test 5
     * "Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)"
     * Should find price 4 because it has higher priority than price 1
     */
    @Test
    void getPrices_Requested_Test_5(){
        //Given:
        LocalDateTime date = LocalDateTime.parse("2020-06-16T21:00:00");
        long productId = 35455L;
        long brandId = 1L;

        // With these parameters, should find sample price 4
        PriceEntity expectedPrice = buildSamplePrice4();
        // save sample prices
        priceRepository.save(buildSamplePrice1());
        priceRepository.save(buildSamplePrice2());
        priceRepository.save(buildSamplePrice3());
        priceRepository.save(expectedPrice);


        // When:
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(
                "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                PriceResponse.class);

        // Then:
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PriceResponse priceResponse = response.getBody();

        assertNotNull(priceResponse);
        assertThat(priceResponse.getBrandId()).isEqualTo(expectedPrice.getBrandId());
        assertThat(priceResponse.getStartDate()).isEqualTo(expectedPrice.getStartDate());
        assertThat(priceResponse.getEndDate()).isEqualTo(expectedPrice.getEndDate());
        assertThat(priceResponse.getPriceList()).isEqualTo(expectedPrice.getPriceListId());
        assertThat(priceResponse.getProductId()).isEqualTo(expectedPrice.getProductId());
        assertThat(priceResponse.getPrice()).isEqualTo(expectedPrice.getPriceAmount().floatValue());
        assertThat(priceResponse.getCurr()).isEqualTo(expectedPrice.getCurrency());
    }


    @Test
    void getPrices_Given_two_prices_dates_overlap_Then_return_top_priority_one() {
        // Given:
        LocalDateTime date = LocalDateTime.parse("2018-06-14T12:00:00");
        Long productId = 1L;
        Long brandId = 1L;

        // Prices overlapping, price2 has top priority
        PriceEntity price1 = PriceEntity.builder()
                .productId(productId)
                .brandId(brandId)
                .priceListId(1L)
                .startDate(LocalDateTime.parse("2018-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2018-12-31T23:59:59"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .build();

        LocalDateTime price2StartDate = LocalDateTime.parse("2018-06-14T10:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2018-10-14T15:00:00");
        BigDecimal price2Amount = BigDecimal.valueOf(40.00);
        String price2Currency = "EUR";
        PriceEntity price2 = PriceEntity.builder()
                .productId(productId)
                .brandId(brandId)
                .priceListId(1L)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .priceAmount(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1);
        priceRepository.save(price2);

        // When:
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(
                "/api/prices?date=" + date + "&product_id=" + productId + "&brand_id=" + brandId,
                PriceResponse.class);

        // Then:
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PriceResponse priceResponse = response.getBody();

        assertNotNull(priceResponse);
        assertThat(priceResponse.getBrandId()).isEqualTo(brandId);
        assertThat(priceResponse.getStartDate()).isEqualTo(price2StartDate);
        assertThat(priceResponse.getEndDate()).isEqualTo(price2EndDate);
        assertThat(priceResponse.getPriceList()).isEqualTo(1L);
        assertThat(priceResponse.getProductId()).isEqualTo(productId);
        assertThat(priceResponse.getPrice()).isEqualTo(price2Amount.floatValue());
        assertThat(priceResponse.getCurr()).isEqualTo(price2Currency);
    }

    @Test
    void getPrices_Given_two_prices_dates_overlap_same_brand_different_product_Then_return_right_one() {
        // Given:
        LocalDateTime price2StartDate = LocalDateTime.parse("2019-07-01T00:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2019-12-31T23:59:59");
        Long brandId = 2L;
        Long price2ProductId = 4L;
        Long price2PriceListId = 4L;
        BigDecimal price2Amount = BigDecimal.valueOf(28.50);
        String price2Currency = "EUR";

        // Dates overlap, same brand and different product
        PriceEntity price1 = PriceEntity.builder()
                .id(1L)
                .productId(2L)
                .brandId(brandId)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2019-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2019-07-14T23:59:59"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.75))
                .currency("EUR")
                .build();

        PriceEntity price2 = PriceEntity.builder()
                .id(2L)
                .productId(price2ProductId)
                .brandId(brandId)
                .priceListId(price2PriceListId)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .priceAmount(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1);
        priceRepository.save(price2);

        // When:
        ResponseEntity<PriceResponse> responseEntity = restTemplate.getForEntity(
                String.format("/api/prices?date=%s&product_id=%d&brand_id=%d",
                        price2StartDate.plusMinutes(5).toString(),
                        price2ProductId,
                        brandId),
                PriceResponse.class);

        // Then:
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        PriceResponse priceResponse = responseEntity.getBody();

        assertNotNull(priceResponse);
        assertEquals(brandId, priceResponse.getBrandId());
        assertEquals(price2StartDate, priceResponse.getStartDate());
        assertEquals(price2EndDate, priceResponse.getEndDate());
        assertEquals(price2PriceListId, priceResponse.getPriceList());
        assertEquals(price2ProductId, priceResponse.getProductId());
        assertEquals(price2Amount.floatValue(), priceResponse.getPrice());
        assertEquals(price2Currency, priceResponse.getCurr());
    }

    @Test
    void getPrices_Given_two_prices_dates_overlap_same_product_different_brand_Then_return_right_one() {
        // Given:
        LocalDateTime price2StartDate = LocalDateTime.parse("2019-07-01T00:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2019-12-31T23:59:59");
        Long price2BrandId = 2L;
        Long productId = 4L;
        Long price2PriceListId = 4L;
        BigDecimal price2Amount = BigDecimal.valueOf(28.50);
        String price2Currency = "EUR";

        // Dates overlap, same product and different brand
        PriceEntity price1 = PriceEntity.builder()
                .id(1L)
                .productId(productId)
                .brandId(5L)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2019-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2019-07-14T23:59:59"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.75))
                .currency("EUR")
                .build();

        // should find this one
        PriceEntity price2 = PriceEntity.builder()
                .id(2L)
                .productId(productId)
                .brandId(price2BrandId)
                .priceListId(price2PriceListId)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .priceAmount(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1);
        priceRepository.save(price2);

        // When:
        ResponseEntity<PriceResponse> responseEntity = restTemplate.getForEntity(
                String.format("/api/prices?date=%s&product_id=%d&brand_id=%d",
                        price2StartDate.plusMinutes(5).toString(),
                        productId,
                        price2BrandId),
                PriceResponse.class);

        // Then:
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        PriceResponse priceResponse = responseEntity.getBody();

        assertNotNull(priceResponse);
        assertEquals(price2BrandId, priceResponse.getBrandId());
        assertEquals(price2StartDate, priceResponse.getStartDate());
        assertEquals(price2EndDate, priceResponse.getEndDate());
        assertEquals(price2PriceListId, priceResponse.getPriceList());
        assertEquals(productId, priceResponse.getProductId());
        assertEquals(price2Amount.floatValue(), priceResponse.getPrice());
        assertEquals(price2Currency, priceResponse.getCurr());
    }

    @Test
    void getPrices_Given_two_prices_dates_do_not_overlap_different_product_different_brand_Then_return_right_one() {
        // Given:
        LocalDateTime price2StartDate = LocalDateTime.parse("2019-07-01T00:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2019-12-31T23:59:59");
        Long price2BrandId = 2L;
        Long price2ProductId = 4L;
        Long price2PriceListId = 4L;
        BigDecimal price2Amount = BigDecimal.valueOf(28.50);
        String price2Currency = "EUR";

        // Dates do not overlap, different brand and different product
        PriceEntity price1 = PriceEntity.builder()
                .id(1L)
                .productId(9L)
                .brandId(5L)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2018-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2018-07-14T23:59:59"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.75))
                .currency("EUR")
                .build();

        // should find this one
        PriceEntity price2 = PriceEntity.builder()
                .id(2L)
                .productId(price2ProductId)
                .brandId(price2BrandId)
                .priceListId(price2PriceListId)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .priceAmount(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1);
        priceRepository.save(price2);

        // When:
        ResponseEntity<PriceResponse> responseEntity = restTemplate.getForEntity(
                String.format("/api/prices?date=%s&product_id=%d&brand_id=%d",
                        price2StartDate.plusMinutes(5).toString(),
                        price2ProductId,
                        price2BrandId),
                PriceResponse.class);

        // Then:
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        PriceResponse priceResponse = responseEntity.getBody();

        assertNotNull(priceResponse);
        assertEquals(price2BrandId, priceResponse.getBrandId());
        assertEquals(price2StartDate, priceResponse.getStartDate());
        assertEquals(price2EndDate, priceResponse.getEndDate());
        assertEquals(price2PriceListId, priceResponse.getPriceList());
        assertEquals(price2ProductId, priceResponse.getProductId());
        assertEquals(price2Amount.floatValue(), priceResponse.getPrice());
        assertEquals(price2Currency, priceResponse.getCurr());
    }

    /**
     * These are the sample prices requested by the technical test documentation
     */
    private PriceEntity buildSamplePrice1() {
        return PriceEntity.builder()
                .id(1L)
                .productId(35455L)
                .brandId(1L)
                .priceListId(1L)
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priority(0)
                .priceAmount(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .build();
    }

    private PriceEntity buildSamplePrice2() {
        return PriceEntity.builder()
                .id(2L)
                .productId(35455L)
                .brandId(1L)
                .priceListId(2L)
                .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
                .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();
    }

    private PriceEntity buildSamplePrice3() {
        return PriceEntity.builder()
                .id(3L)
                .productId(35455L)
                .brandId(1L)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(30.50))
                .currency("EUR")
                .build();
    }


    private PriceEntity buildSamplePrice4() {
        return PriceEntity.builder()
                .id(4L)
                .productId(35455L)
                .brandId(1L)
                .priceListId(4L)
                .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .priority(1)
                .priceAmount(BigDecimal.valueOf(38.95))
                .currency("EUR")
                .build();
    }


}
