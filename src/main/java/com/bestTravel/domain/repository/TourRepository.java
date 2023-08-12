package com.bestTravel.domain.repository;

import com.bestTravel.domain.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Repository
public interface TourRepository extends JpaRepository<TourEntity, Long> {
}
