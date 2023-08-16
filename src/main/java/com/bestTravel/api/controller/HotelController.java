package com.bestTravel.api.controller;

import com.bestTravel.api.model.response.HotelResponse;
import com.bestTravel.infrastructure.abstract_services.HotelService;
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
 * date: 08/2023
 */

@RestController
@RequestMapping("/hotels")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Best Travel - Hoteles")
public class HotelController {
    private final HotelService hotelService;

    @GetMapping
    @Operation(summary = "muestra una lista de hoteles paginados y ordenados",
            description = "muestra una lista de hoteles paginados y ordenados")
    @Parameters({
            @Parameter(name = "page", description = "numero depagina"),
            @Parameter(name = "size", description = "elementos a mostrar"),
            @Parameter(name = "sortType", description = "ordenamiento")
    })
    public ResponseEntity<Page<HotelResponse>> getAll(@RequestParam Integer page,
                                                      @RequestParam Integer size,
                                                      @RequestHeader SortType sortType){
        log.info("#### endpoint buscar hoteles paginados y ordenados");
        if (Objects.isNull(sortType)) sortType = SortType.NONE;
        var response = hotelService.getAll(page, size, sortType);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/less-price")
    @Operation(summary = "muestra una lista de hoteles con precio menor al indicado",
            description = "muestra una lista de hoteles con precio menor al indicado")
    @Parameter(name = "price", description = "precio")
    public ResponseEntity<Set<HotelResponse>> getpriceLess(@RequestParam BigDecimal price){
        log.info("#### endpoint buscar hoteles con precio menor");
        var response = hotelService.readLesPrice(price);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/price-between")
    @Operation(summary = "muestra una lista de hotes con precios entre los indicados",
            description = "muestra una lista de hotes con precios entre los indicados")
    @Parameters({
            @Parameter(name = "min", description = "precio minimo"),
            @Parameter(name = "max", description = "precio maximo")
    })
    public ResponseEntity<Set<HotelResponse>> getflyBetweenPrice(@RequestParam BigDecimal min,
                                                               @RequestParam BigDecimal max){
        log.info("#### endpoint buscar hoteles entre precios");
        var response = hotelService.readBetweenPrice(min, max);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }

    @GetMapping("/rating")
    @Operation(summary = "muestra una lista de hoteles con rating superior al indicado",
            description = "muestra una lista de hoteles con rating superior al indicado")
    @Parameter(name = "rating", description = "rating")
    public ResponseEntity<Set<HotelResponse>> getFlyBetweenOriginDestiny(@RequestParam Integer rating) {
        log.info("#### endpoint buscar hoteles con rating mayores");
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;
        var response = hotelService.readGreaterThan(rating);
        return response.isEmpty()?ResponseEntity.noContent().build():
                ResponseEntity.ok(response);
    }
}
