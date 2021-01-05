package no.nsd.qddt.domain.agency.web;

import no.nsd.qddt.classes.AbstractController;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.agency.AgencyEditJson;
import no.nsd.qddt.domain.agency.AgencyService;
import no.nsd.qddt.classes.exception.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/agency")
public class AgencyController extends AbstractController {

    private final AgencyService service;

    @Autowired
    public AgencyController(AgencyService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public AgencyEditJson get(@PathVariable("id") UUID id) {
        return new AgencyEditJson(service.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Agency> getAll()  {
        super.LOG.info( "Agency get all" );
        return service.getAll();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public AgencyEditJson update(@RequestBody Agency agency) {
        return new AgencyEditJson(service.save(agency));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public AgencyEditJson create(@RequestBody Agency agency) {
        return new AgencyEditJson(service.save(agency));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }


    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<Agency> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                Pageable pageable) {
        try {
            return service.findByNamePageable(name, pageable);
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            throw ex;
        }
    }


//    public ResponseEntity getBy(@RequestParam(value = "name",defaultValue = "%") String name,
//                                Pageable pageable, PagedResourcesAssembler assembler) {
//        try {
//            Page<Agency> agencies = service.findByNamePageable(name, pageable);
////            agencies.forEach( Agency::getUsers );
//            return assembler.toModel(agencies), HttpStatus.OK);
//        } catch (Exception ex){
//            StackTraceFilter.println(ex.getStackTrace());
//            throw ex;
//        }
//    }

}
