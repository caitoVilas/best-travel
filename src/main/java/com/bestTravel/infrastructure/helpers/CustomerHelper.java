package com.bestTravel.infrastructure.helpers;

import com.bestTravel.domain.repository.CustomerRepository;
import com.bestTravel.util.enums.Tables;
import com.bestTravel.util.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author claudio.vilas
 * data: 08/2023
 */

@Component
@Transactional
@RequiredArgsConstructor
public class CustomerHelper {
    private final CustomerRepository customerRepository;

    public void incrase(String customerId, Class<?> type){
        var customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));
        switch (type.getSimpleName()){
            case "TourService" -> customer.setTotalTours(customer.getTotalTours() + 1);
            case "TicketService" -> customer.setTotalFlights(customer.getTotalFlights() + 1);
            case "ReservationService" -> customer.setTotalLodgings(customer.getTotalLodgings() + 1);
        }
        customerRepository.save(customer);
    }
}
