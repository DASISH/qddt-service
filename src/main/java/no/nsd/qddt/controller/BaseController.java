package no.nsd.qddt.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface BaseController<T> {

    List<T> getAll();
    HttpEntity<PagedResources<T>> getAll(Pageable pageable, PagedResourcesAssembler assembler);

    T getOne(Long id);
    T getOne(UUID id);

    T create(T instance);
    void delete(T instance);

}
