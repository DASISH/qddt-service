package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.service.AgencyService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/agency")
public class AgencyController implements BaseController<Agency> {
    private AgencyService agencyService;

    @Autowired
    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Agency> getAll() {
        return agencyService.findAll();
    }


    @Override
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public HttpEntity<PagedResources<Agency>> getAll(org.springframework.data.domain.Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Agency> instances = agencyService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Agency getOne(@PathVariable("id") Long id) {
        return agencyService.findById(id);
    }

    @Override
    public Agency getOne( UUID id) {
        throw new NotImplementedException();
    }

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Agency create(Agency agency) {
        return agencyService.save(agency);
    }

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void delete(Agency instance) {
        agencyService.delete(instance);
    }

}
