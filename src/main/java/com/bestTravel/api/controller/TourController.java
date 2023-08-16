package com.bestTravel.api.controller;

import com.bestTravel.api.model.request.TourRequest;
import com.bestTravel.api.model.response.TourResponse;
import com.bestTravel.infrastructure.abstract_services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@RestController
@RequestMapping("/tours")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Best Travel - Tour")
public class TourController {
    private final TourService tourService;

    @PostMapping
    @Operation(summary = "guarda un tour en el sistema",
            description = "recibe un id de cliente una lista de vuelos y una lista de hoteles")
    @Parameters({
            @Parameter(name = "idCustomer", description = "id del cliente"),
            @Parameter(name = "flights", description = "lista de vuelos"),
            @Parameter(name = "hotels", description = "lista de hoteles")
    })
    public ResponseEntity<TourResponse> create(@RequestBody TourRequest request){
        log.info("#### endpoint creacion de tours");
        return ResponseEntity.ok(tourService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "muestra un tour por id",
            description = "muestra un tour por id si existe")
    @Parameter(name = "id", description = "id del tour")
    public ResponseEntity<TourResponse> read(@PathVariable Long id){
        log.info("#### endpoint buscar tour por id");
        return ResponseEntity.ok(tourService.read(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "elimina un tour por id",
            description = "elimina un tour por id si existe")
    @Parameter(name = "id", description = "id del tour")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        log.info("#### endpoint eliminar tour por id");
        tourService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/remove-ticket/{ticketId}/{tourId}")
    @Operation(summary = "remueve un ticket de un tour",
            description = "remueve un ticket de un tour")
    @Parameters({
            @Parameter(name = "ticketId", description = "id del ticket"),
            @Parameter(name = "tourId", description = "id del tour")
    })
    public ResponseEntity<Void> removeTcket(@PathVariable UUID ticketId,
                                            @PathVariable Long tourId){
        log.info("#### endpoint remover ticket de tour");
        tourService.removeTicket(ticketId, tourId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/add-ticket/{flyId}/{tourId}")
    @Operation(summary = "agrega un ticket de un tour",
            description = "agrega un ticket de un tour")
    @Parameters({
            @Parameter(name = "flyId", description = "id del vuelo"),
            @Parameter(name = "tourId", description = "id del tour")
    })
    public ResponseEntity<Map<String,UUID>> addTicket(@PathVariable Long flyId, @PathVariable Long tourId){
        log.info("#### endpoint agregar ticket a tour");
        var response = tourService.addTicket(flyId, tourId);
        return ResponseEntity.ok(Collections.singletonMap("ticket id", response));
    }

    @PatchMapping("/remove-reservation/{reservationId}/{tourId}")
    @Operation(summary = "remueve una reservacion de un tour",
            description = "remueve unreservacion de un tour")
    @Parameters({
            @Parameter(name = "reservationId", description = "id de la reservacion"),
            @Parameter(name = "tourId", description = "id del tour")
    })
    public ResponseEntity<Void> removeReservation(@PathVariable UUID reservationId,
                                                  @PathVariable Long tourId){
        log.info("##### endpoint remover reservacion de tour");
        tourService.removeReservation(reservationId, tourId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/add-reservation/{hotelId}/{tourId}/{totalDays}")
    @Operation(summary = "agrega una reservacion a un tour",
            description = "agrega una reservacion a un tour")
    @Parameters({
            @Parameter(name = "hotelId", description = "id del hotel"),
            @Parameter(name = "tourId", description = "id del tour"),
            @Parameter(name = "totalDays", description = "total de dias")
    })
    public ResponseEntity<Map<String, UUID>> addReservation(@PathVariable Long hotelId,
                                                            @PathVariable Long tourId,
                                                            @PathVariable Integer totalDays){
        log.info("#### endpoint agregar reservacion a tour");
        var response = tourService.addReservation(hotelId, tourId, totalDays);
        return ResponseEntity.ok(Collections.singletonMap("reservation id", response));
    }
}
