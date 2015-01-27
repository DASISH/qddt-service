package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.service.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/agency")
public class AgencyController {
    private AgencyService agencyService;

    @Autowired
    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Agency> getAll() {
        return agencyService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Agency getOne(@PathVariable("id") Long id) {
        return agencyService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Agency create(Agency agency) {
        return agencyService.save(agency);
    }

}
