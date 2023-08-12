package com.bestTravel.domain.repository;

import com.bestTravel.domain.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
}
