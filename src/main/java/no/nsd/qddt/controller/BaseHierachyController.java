package no.nsd.qddt.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface BaseHierachyController<T>  {

    HttpEntity<PagedResources<T>> getThreadbyId(Long id, Pageable pageable, PagedResourcesAssembler assembler);

    HttpEntity<PagedResources<T>> getThreadbyGuid(UUID id, Pageable pageable, PagedResourcesAssembler assembler);


}
