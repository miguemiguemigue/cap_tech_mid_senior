package com.cap.midsenior.prices_api.adapter.out.database.h2.adapter;

import com.cap.midsenior.prices_api.adapter.out.database.h2.entity.PriceEntity;
import com.cap.midsenior.prices_api.adapter.out.database.h2.repository.PriceRepository;
import com.cap.midsenior.prices_api.domain.model.Price;
import com.cap.midsenior.prices_api.domain.ports.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final PriceRepository priceRepository;

    @Override
    public List<Price> getPriceByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {
        return priceRepository.findPricesByDateProductAndBrand(date, productId, brandId)
                .stream()
                .map(PriceEntity::toDomain)
                .collect(Collectors.toList());
    }

}
