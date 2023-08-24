package com.bestTravel.infrastructure.services;

import com.bestTravel.domain.entity.AppUserEntity;
import com.bestTravel.domain.entity.Role;
import com.bestTravel.domain.repository.AppUserRepository;
import com.bestTravel.domain.repository.RolRepository;
import com.bestTravel.infrastructure.abstract_services.ModifyUserService;
import com.bestTravel.util.enums.RoleName;
import com.bestTravel.util.enums.Tables;
import com.bestTravel.util.exception.UserNameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AppUserService implements ModifyUserService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final RolRepository repository;

    @Override
    public Map<String, Boolean> enabled(String username) {
        log.info("---> inicio servicio habilitar/desabilitar usuario");
        log.info("---> buscando usuario por username {}", username);
        var user = appUserRepository.findByUsername(username)
                .orElseThrow(()-> new UserNameNotFoundException(Tables.users.name()));
        user.setEnabled(!user.isEnabled());
        log.info("---> guardando nuevo estado...");
        var saveUser = appUserRepository.save(user);
        log.info("---> finalzado servicio hebilitar/desabilitar usuario");
        return Collections.singletonMap(saveUser.getUsername(), saveUser.isEnabled());
    }

    @Override
    public Map<String, List<String>> addRole(String username, RoleName role) {
        log.info("---> inicio servicio agregar rol");
        log.info("---> buscando usuario por username {}", username);
        var user = appUserRepository.findByUsername(username)
                .orElseThrow(()-> new UserNameNotFoundException(Tables.users.name()));
        var roleUser = repository.findByGrantedAuthorities(role);
        List<Role> roles = user.getRole();
        if (roles.contains(roleUser)){
            log.error("ERROR: el rol ya esta asignado");
            throw new RuntimeException();
        }
        roles.add(roleUser);
        user.setRole(roles);
        log.info("---> agregando rol...");
        var userSaved = appUserRepository.save(user);
        log.info("---> finalizado servicio agregar rol");
        List<String> rolesNuevos = new ArrayList<>();
        userSaved.getRole().forEach(r -> rolesNuevos.add(r.getGrantedAuthorities().toString()));
        return Collections.singletonMap(userSaved.getUsername(), rolesNuevos);
    }

    @Override
    public Map<String, List<String>> removeRole(String username, RoleName role) {
        log.info("---> inicio servicio remover rol");
        log.info("---> buscando usuario por username {}", username);
        var user = appUserRepository.findByUsername(username)
                .orElseThrow(()-> new UserNameNotFoundException(Tables.users.name()));
        var roleUser = repository.findByGrantedAuthorities(role);
        List<Role> roles = user.getRole();
        if (!roles.contains(roleUser)){
            log.error("ERROR: el usuario no tiene ese rol");
            throw new RuntimeException();
        }
        roles.remove(roleUser);
        user.setRole(roles);
        log.info("---> removiendo rol...");
        var userSaved = appUserRepository.save(user);
        log.info("---> finalizado servicio remover rol");
        List<String> rolesNuevos = new ArrayList<>();
        userSaved.getRole().forEach(r -> rolesNuevos.add(r.getGrantedAuthorities().toString()));
        return Collections.singletonMap(userSaved.getUsername(), rolesNuevos);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = appUserRepository.findByUsername(username)
                .orElseThrow(() ->new UsernameNotFoundException(Tables.users.name()));
        return mapUser(user);
    }

    private static UserDetails mapUser(AppUserEntity user){
        Set<GrantedAuthority> authorities = user.getRole()
                .stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getGrantedAuthorities().toString()))
                .collect(Collectors.toSet());
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                true, authorities);

    }
}
