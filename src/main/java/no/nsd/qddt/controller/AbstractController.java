package no.nsd.qddt.controller;

import no.nsd.qddt.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Stig Norland
 */
public abstract class AbstractController<T> {

    AbstractService<T> service;

//    @Autowired
//    public AbstractController()

}
