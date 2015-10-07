package no.nsd.qddt.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
