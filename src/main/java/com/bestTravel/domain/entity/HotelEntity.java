package com.bestTravel.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Entity
@Table(name = "hotel")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String address;
    private Integer rating;
    private BigDecimal price;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL,
               fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ReservationEntity> reservation;
}
