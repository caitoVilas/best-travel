package com.bestTravel.api.controller;

import com.bestTravel.api.model.response.FlyResponse;
import com.bestTravel.infrastructure.abstract_services.FlyService;
import com.bestTravel.util.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * @author claudio.vilas
 * date. 08/2023
 */

@RestController
@RequestMapping("/fly")
@Slf4j
@RequiredArgsConstructor
public class FlyController {
    private final FlyService flyService;

    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(@RequestParam Integer page,
                                                    @RequestParam Integer size,
                                                    @RequestHeader SortType sortType){
        log.info("#### endpoint buscar vuelos paginados y ordenados");
        if (Objects.isNull(sortType)) sortType = SortType.NONE;
        var response = flyService.getAll(page, size, sortType);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/less-price")
    public ResponseEntity<Set<FlyResponse>> getpriceLess(@RequestParam BigDecimal price){
        log.info("#### endpoint buscar vuelos con precio menor");
        var response = flyService.readLesPrice(price);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/price-between")
    public ResponseEntity<Set<FlyResponse>> getflyBetweenPrice(@RequestParam BigDecimal min,
                                                               @RequestParam BigDecimal max){
        log.info("#### endpoint buscar vuelos entre precios");
        var response = flyService.readBetweenPrice(min, max);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/origin-destiny")
    public ResponseEntity<Set<FlyResponse>> getFlyBetweenOriginDestiny(@RequestParam String origin,
                                                                       @RequestParam String destiny){
        log.info("#### endpoint buscar vuelos entre origen y destino");
        var response = flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }
}
