package com.bestTravel.api.controller;

import com.bestTravel.api.model.request.TicketRequest;
import com.bestTravel.api.model.response.TicketResponse;
import com.bestTravel.infrastructure.abstract_services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Slf4j
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody TicketRequest request){
        log.info("#### endpoint creacion de tickets");
        return ResponseEntity.ok(ticketService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> read(@PathVariable UUID id){
        log.info("#### endpoint buscar ticket por id");
        return ResponseEntity.ok(ticketService.read(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> update(@PathVariable UUID id,
                                                 @RequestBody TicketRequest request){
        log.info("#### endpoint actualizar ticket");
        return ResponseEntity.ok(ticketService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        log.info("#### endpoint eliminar ticket por id");
        ticketService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/price/{idFly}")
    public ResponseEntity<Map<String, BigDecimal>> viewPrice(@PathVariable Long idFly){
        log.info("#### endpoint consultar precio ticket");
        return ResponseEntity.ok(Collections.singletonMap("Precio",ticketService.findPrice(idFly)));
    }
}
