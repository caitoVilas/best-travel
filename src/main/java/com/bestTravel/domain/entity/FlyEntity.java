package com.bestTravel.domain.entity;

import com.bestTravel.util.Aeroline;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Entity
@Table(name = "fly")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;
    private BigDecimal price;
    @Column(length = 20)
    private String originName;
    @Column(length = 20)
    private String destinyName;
    @Enumerated(EnumType.STRING)
    private Aeroline aeroline;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "fly", cascade = CascadeType.ALL,
               fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<TicketEntity> tickets;
}
