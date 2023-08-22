package com.bestTravel.domain.repository;

import com.bestTravel.domain.entity.Role;
import com.bestTravel.util.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author claudio.vilas
 * date: 08/2023
 */
@Repository
public interface RolRepository extends JpaRepository<Role, Long> {
    Role findByGrantedAuthorities(RoleName grantedAuthorities);
}
