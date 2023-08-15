package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.api.model.response.HotelResponse;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

public interface HotelService extends CatalogService<HotelResponse> {
    Set<HotelResponse> readGreaterThan(Integer rating);
}
