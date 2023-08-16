package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.util.enums.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author claudio.vilas
 * date: 08/2023
 */
public interface CatalogService<R> {
    String FIELD_BY_SORT = "price";
    Page<R> getAll(Integer page, Integer size, SortType sortType);
    Set<R> readLesPrice(BigDecimal price);
    Set<R> readBetweenPrice(BigDecimal min, BigDecimal max);
}
