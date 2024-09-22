package com.cap.midsenior.prices_api.adapter.in.rest.mapper;

import com.cap.midsenior.prices_api.adapter.in.rest.dto.PriceResponse;
import com.cap.midsenior.prices_api.domain.model.Price;

public class PriceResponseMapper {

    public static PriceResponse fromDomain(Price price) {
        if (price == null) {
            return null;
        }

        // OpenAPI generator doesn't provide builder function
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setProductId(price.getProductId());
        priceResponse.setBrandId(price.getBrandId());
        priceResponse.setPriceList(price.getPriceListId());
        priceResponse.setStartDate(price.getStartDate());
        priceResponse.setEndDate(price.getEndDate());
        priceResponse.setPrice(price.getPriceAmount().floatValue());
        priceResponse.setCurr(price.getCurrency());

        return priceResponse;

    }
}
