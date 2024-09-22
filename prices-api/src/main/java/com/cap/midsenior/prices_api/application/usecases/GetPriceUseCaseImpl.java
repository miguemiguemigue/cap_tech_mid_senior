package com.cap.midsenior.prices_api.application.usecases;

import com.cap.midsenior.prices_api.domain.model.Price;
import com.cap.midsenior.prices_api.domain.ports.in.GetPriceUseCase;
import com.cap.midsenior.prices_api.domain.ports.out.PriceRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Component
public class GetPriceUseCaseImpl implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public List<Price> getPriceByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {
        return priceRepositoryPort.getPriceByDateProductAndBrand(date, productId, brandId);
    }
}
