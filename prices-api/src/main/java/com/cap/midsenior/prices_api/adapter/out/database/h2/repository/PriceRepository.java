package com.cap.midsenior.prices_api.adapter.out.database.h2.repository;

import com.cap.midsenior.prices_api.adapter.out.database.h2.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query("SELECT p FROM PriceEntity p WHERE :date BETWEEN p.startDate AND p.endDate " +
            "AND p.productId = :productId AND p.brandId = :brandId")
    List<PriceEntity> findPricesByDateProductAndBrand(
            @Param("date") LocalDateTime date,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId);
}
