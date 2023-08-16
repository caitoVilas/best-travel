package com.bestTravel.api.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourHotelRequest implements Serializable {
    @Positive
    @NotNull(message = "id hotel is mandatory")
    private Long id;
    @Min(value = 1, message = "min one days to make reservation")
    @Max(value = 30, message = "max 30 days to make reservation")
    @NotNull(message = "totalDays is mandatory")
    private Integer totalDays;
}
