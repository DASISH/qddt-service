package no.nsd.qddt.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;

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
