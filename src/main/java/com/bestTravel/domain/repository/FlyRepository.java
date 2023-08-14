package com.bestTravel.domain.repository;

import com.bestTravel.domain.entity.FlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Repository
public interface FlyRepository extends JpaRepository<FlyEntity, Long> {
    @Query("SELECT f FROM FlyEntity f WHERE f.price < :price")
    Set<FlyEntity> selectLessPrice(BigDecimal price);
    @Query("SELECT f FROM FlyEntity f WHERE f.price BETWEEN :min AND :max")
    Set<FlyEntity> selecBetweenPrice(BigDecimal min, BigDecimal max);
    @Query("SELECT f FROM FlyEntity f WHERE f.originName = :origin AND f.destinyName = :destiny")
    Set<FlyEntity> selecOriginDestiny(String origin, String destiny);
    @Query("SELECT f FROM FlyEntity f JOIN FETCH f.tickets t WHERE t.id = :id")
    Optional<FlyEntity> selectByTicketid(UUID id);
}
