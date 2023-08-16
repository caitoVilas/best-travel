package com.bestTravel.infrastructure.services;

import com.bestTravel.api.model.response.HotelResponse;
import com.bestTravel.domain.entity.HotelEntity;
import com.bestTravel.domain.repository.HotelRepository;
import com.bestTravel.infrastructure.abstract_services.HotelService;
import com.bestTravel.util.enums.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;


    @Override
    public Page<HotelResponse> getAll(Integer page, Integer size, SortType sortType) {
        log.info("---> inicio servicio obtener hoteles paginados y ordenados");
        PageRequest pageRequest = null;
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        log.info("---> buscando hoteles...");
        log.info("---> finalizado servicio buscar hoteles ordenados y paginados");
        return hotelRepository.findAll(pageRequest).map(this::mapToDto);
    }

    @Override
    public Set<HotelResponse> readLesPrice(BigDecimal price) {
        log.info("---> inicio servicio buscar hoteles por precio menor {}", price);
        log.info("---> buscando hoteles...");
        log.info("---> finalizado servicio buscar hoteles por precio menor");
        return hotelRepository.findByPriceLessThan(price)
                .stream().map(this::mapToDto).collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        log.info("---> inicio servicio buscar hoteles entre precios {} y {}", min, max);
        log.info("---> buscando hoteles...");
        log.info("---> finalizado servicio buscar hoteles entre precios");
        return hotelRepository.findByPriceIsBetween(min, max)
                .stream().map(this::mapToDto).collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readGreaterThan(Integer rating) {
        log.info("---> inicio servicio buscar hotel con rating mayor a {}", rating);
        log.info("---> buscando hoteles...");
        log.info("---> finalizado servicio buscar hoteles con rating mayor");
        return hotelRepository.findByRatingGreaterThan(rating)
                .stream().map(this::mapToDto).collect(Collectors.toSet());
    }

    private HotelResponse mapToDto(HotelEntity entity){
        HotelResponse response = new HotelResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
