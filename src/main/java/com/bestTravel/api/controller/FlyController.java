package com.bestTravel.api.controller;

import com.bestTravel.api.model.response.FlyResponse;
import com.bestTravel.infrastructure.abstract_services.FlyService;
import com.bestTravel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Best Travel - Vuelos")
public class FlyController {
    private final FlyService flyService;

    @GetMapping
    @Operation(summary = "Muestra los vuelos paginados y ordenados",
               description = "Muestra los vuelos paginados y ordenados")
    @Parameters({
            @Parameter(name = "page", description = "numero de pagina"),
            @Parameter(name = "size", description = "elementos a mostrar"),
            @Parameter(name = "sortType", description = "ordenamiento")
    })
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
    @Operation(summary = "muestra una lista de vuelos con precios menor que el indicado",
              description = "muestra una lista de vuelos con precios menor que el indicado")
    @Parameter(name = "price", description = "precio")
    public ResponseEntity<Set<FlyResponse>> getpriceLess(@RequestParam BigDecimal price){
        log.info("#### endpoint buscar vuelos con precio menor");
        var response = flyService.readLesPrice(price);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/price-between")
    @Operation(summary = "muestra una lista de vuelos cuyo precio este entre los indicados",
              description = "muestra una lista de vuelos cuyo precio este entre los indicados")
    @Parameters({
            @Parameter(name = "min", description = "precio minimo"),
            @Parameter(name = "max", description = "precio maximo")
    })
    public ResponseEntity<Set<FlyResponse>> getflyBetweenPrice(@RequestParam BigDecimal min,
                                                               @RequestParam BigDecimal max){
        log.info("#### endpoint buscar vuelos entre precios");
        var response = flyService.readBetweenPrice(min, max);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/origin-destiny")
    @Operation(summary = "muestra una lista de vuelos entre origen y destino indicados",
            description = "muestra una lista de vuelos entre origen y destino indicados")
    @Parameters({
            @Parameter(name = "origin", description = "origen"),
            @Parameter(name = "destiny", description = "destino")
    })
    public ResponseEntity<Set<FlyResponse>> getFlyBetweenOriginDestiny(@RequestParam String origin,
                                                                       @RequestParam String destiny){
        log.info("#### endpoint buscar vuelos entre origen y destino");
        var response = flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }
}
