package com.bestTravel.infrastructure.abstract_services;

/**
 * @author claudio.vilas
 * date. 08/2023
 * @param <RQ>
 * @param <RS>
 * @param <ID>
 */

public interface SimpleCrudService<RQ,RS,ID>{
    RS create(RQ request);
    RS read(ID id);
    void delete(ID id);
}
