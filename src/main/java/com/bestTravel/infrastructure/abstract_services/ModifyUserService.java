package com.bestTravel.infrastructure.abstract_services;

import com.bestTravel.util.enums.RoleName;

import java.util.List;
import java.util.Map;

/**
 * @author claudio.vilas
 * date: 08/2023
 */
public interface ModifyUserService {
    Map<String, Boolean> enabled(String username);
    Map<String, List<String>> addRole(String username, RoleName role);
    Map<String, List<String>> removeRole(String username, RoleName role);
 }
