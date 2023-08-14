package com.bestTravel.infrastructure.services;

import com.bestTravel.api.model.response.FlyResponse;
import com.bestTravel.domain.entity.FlyEntity;
import com.bestTravel.domain.repository.FlyRepository;
import com.bestTravel.infrastructure.abstract_services.FlyService;
import com.bestTravel.util.SortType;
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
 * date:
 */

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FlyServiceImpl implements FlyService {
    private final FlyRepository flyRepository;

    @Override
    public Page<FlyResponse> getAll(Integer page, Integer size, SortType sortType) {
        log.info("---> inicio servicio obtener vuelos paginados y ordenados");
        PageRequest pageRequest = null;
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        log.info("---> busando vuelos...");
        log.info("---> finalizado servicio obtener vuelos paginados y ordenados");
        return flyRepository.findAll(pageRequest).map(this::mapTodto);
    }

    @Override
    public Set<FlyResponse> readLesPrice(BigDecimal price) {
        log.info("---> inicio servicio buscar vuelos con precio menor a {}", price);
        log.info("---> buscando vuelos...");
        log.info("---> finalizado servicio buscar vuelos con precio menor");
        return flyRepository.selectLessPrice(price)
                .stream().map(this::mapTodto).collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        log.info("---> inicio servicio buscar vuelos entre precios {} y {}", min, max);
        log.info("---> buscando vuelos....");
        log.info("---> finalizado servicio buscar vuelos entre precios");
        return flyRepository.selecBetweenPrice(min, max)
                .stream().map(this::mapTodto).collect(Collectors.toSet());
    }

    @Override
    public Set<FlyResponse> readByOriginDestiny(String origin, String destiny) {
        log.info("---> inicio servicio buscar vuelos con origen {} y destino {}", origin, destiny);
        log.info("---> buscando vuelos....");
        log.info("---> finalizado servicio buscar vuelos entre origen y destino");
        return flyRepository.selecOriginDestiny(origin, destiny)
                .stream().map(this::mapTodto).collect(Collectors.toSet());
    }

    private FlyResponse mapTodto(FlyEntity entity){
        FlyResponse respone = new FlyResponse();
        BeanUtils.copyProperties(entity, respone);
        return respone;
    }
}
