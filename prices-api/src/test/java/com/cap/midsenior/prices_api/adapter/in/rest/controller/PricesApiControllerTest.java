package com.cap.midsenior.prices_api.adapter.in.rest.controller;

import com.cap.midsenior.prices_api.application.services.PriceService;
import com.cap.midsenior.prices_api.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PricesApiController.class})
class PricesApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void getPrices_Given_valid_arguments_Then_return_price() throws Exception {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        LocalDateTime priceStartDate = LocalDateTime.of(2024, 9, 20, 0, 0, 0);
        LocalDateTime priceEndDate = LocalDateTime.of(2024, 9, 22, 0, 0, 0);
        Long priceListId = 1L;
        int priority = 2;
        BigDecimal priceAmount = BigDecimal.valueOf(20);
        String currency = "EUR";
        Price price = Price.builder()
                .brandId(brandId)
                .productId(productId)
                .priceListId(priceListId)
                .startDate(priceStartDate)
                .endDate(priceEndDate)
                .priority(priority)
                .priceAmount(priceAmount)
                .currency(currency)
                .build();

        when(priceService.findByDateProductAndBrand(date, productId, brandId)).thenReturn(Optional.of(price));

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", date.toString())
                        .param("product_id", productId.toString())
                        .param("brand_id", brandId.toString()))
                // Then:
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand_id").value(brandId.intValue()))
                .andExpect(jsonPath("$.start_date").value(priceStartDate.format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$.end_date").value(priceEndDate.format(DATE_TIME_FORMATTER)))
                .andExpect(jsonPath("$.price_list").value(priceListId.intValue()))
                .andExpect(jsonPath("$.product_id").value(productId.intValue()))
                .andExpect(jsonPath("$.price").value(priceAmount.doubleValue()))
                // keep priority out of api response
                .andExpect(jsonPath("$.priority").doesNotExist());

        verify(priceService).findByDateProductAndBrand(eq(date), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_invalid_date_Then_return_bad_request() throws Exception {
        // Given:
        String invalidDate = "invalidDate";
        Long productId = 1L;
        Long brandId = 1L;

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", invalidDate)
                        .param("product_id", productId.toString())
                        .param("brand_id", brandId.toString()))
                // Then:
                .andExpect(status().isBadRequest());

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_null_date_Then_return_bad_request() throws Exception {
        // Given:
        String invalidDate = "";
        Long productId = 1L;
        Long brandId = 1L;

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", invalidDate)
                        .param("product_id", productId.toString())
                        .param("brand_id", brandId.toString()))
                // Then:
                .andExpect(status().isBadRequest());

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_invalid_product_id_Then_return_bad_request() throws Exception {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = -1L;
        Long brandId = 1L;

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", date.toString())
                        .param("product_id", productId.toString())
                        .param("brand_id", brandId.toString()))
                // Then:
                .andExpect(status().isBadRequest());

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_null_product_id_Then_return_bad_request() throws Exception {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = null;
        Long brandId = 1L;

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", date.toString())
                        .param("product_id", "")
                        .param("brand_id", brandId.toString()))
                // Then:
                .andExpect(status().isBadRequest());

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_invalid_brand_id_Then_return_bad_request() throws Exception {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = -1L;

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", date.toString())
                        .param("product_id", productId.toString())
                        .param("brand_id", brandId.toString()))
                // Then:
                .andExpect(status().isBadRequest());

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_null_brand_id_Then_return_bad_request() throws Exception {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = null;

        // When:
        mockMvc.perform(get("/api/prices")
                        .param("date", date.toString())
                        .param("product_id", productId.toString())
                        .param("brand_id", ""))
                // Then:
                .andExpect(status().isBadRequest());

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }



}