package no.nsd.qddt.controller;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.agency.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/agency")
public class AgencyController extends AbstractController<Agency,UUID> {

    private AgencyService agencyService;

    @Autowired
    public AgencyController(AgencyService agencyService) {
        super(agencyService);
        this.agencyService = agencyService;
    }


//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Agency getOneByGuid(@PathVariable("id") UUID id) {
//        return agencyService.findByGuid(id);
//    }
//

}
