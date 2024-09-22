package com.cap.midsenior.prices_api.adapter.out.database.h2.entity;

import com.cap.midsenior.prices_api.domain.model.Price;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "price_list_id")
    private Long priceListId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "price")
    private BigDecimal priceAmount;

    @Column(name = "currency")
    private String currency;

    public static Price toDomain(PriceEntity priceEntity) {
        if (priceEntity == null) {
            return null;
        }
        return Price.builder()
                .brandId(priceEntity.getBrandId())
                .productId(priceEntity.getProductId())
                .priceListId(priceEntity.getPriceListId())
                .startDate(priceEntity.getStartDate())
                .endDate(priceEntity.getEndDate())
                .priority(priceEntity.getPriority())
                .priceAmount(priceEntity.getPriceAmount())
                .currency(priceEntity.getCurrency())
                .build();
    }

}