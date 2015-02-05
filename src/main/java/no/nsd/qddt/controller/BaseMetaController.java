package no.nsd.qddt.controller;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface BaseMetaController<T> {

    T create(T instance);
    void delete(T instance);

    List<T> getByFirst(Long firstId);

    List<T> getBySecond(Long secondId);


}
