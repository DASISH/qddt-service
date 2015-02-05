package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.service.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/agency")
public class AgencyController extends AbstractController<Agency> {

//    @Autowired
//    public AgencyController(AgencyService agencyService) {
//        this.agencyService = agencyService;
//    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Agency getOne(@PathVariable("id") UUID id) {
        return service.findById(id);
    }


}
