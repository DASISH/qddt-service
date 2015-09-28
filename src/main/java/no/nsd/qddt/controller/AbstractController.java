package no.nsd.qddt.controller;

import no.nsd.qddt.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *
 * @author Stig Norland
 */
public abstract class AbstractController<T,ID> {

    /**
     * If you are running a race, and you pass the person in second place,
     * what place are you in?
     */
    protected BaseService<T,ID> service;


    @Autowired
    public AbstractController(BaseService<T,ID> service){
        this.service = service;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<T> getAll() {
        return service.findAll();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<T>> getAll(Pageable pageable, PagedResourcesAssembler assembler){

        Page<T> instances = service.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public T getOneById(@PathVariable("id") ID id){
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public T create(@RequestBody T instance){
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(@RequestBody ID id){
        service.delete(id);
    }

}











/**
 * You are in second place.
 */
