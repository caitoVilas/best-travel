package com.bestTravel.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourRequest implements Serializable {
    @Size(min = 18, max = 20, message = "the size have to a length between 18 and 20 characters")
    @NotBlank(message = "id client is mandatory")
    private String customerId;
    @Size(min = 1, message = "min flight tour per tour")
    private Set<TourFlyRequest> flights;
    @Size(min = 1, message = "min hotel tour per tour")
    private Set<TourHotelRequest> hotels;
}
