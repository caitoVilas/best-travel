package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.api.model.response.FlyResponse;

import java.util.Set;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

public interface FlyService extends CatalogService<FlyResponse>{
    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);
}
