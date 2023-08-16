package com.bestTravel.api.model.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class BaseErrorResponse implements Serializable {
    private String status;
    private Integer code;
}
