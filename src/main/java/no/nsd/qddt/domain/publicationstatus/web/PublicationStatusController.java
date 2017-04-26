package no.nsd.qddt.domain.publicationstatus.web;

import com.fasterxml.jackson.annotation.JsonView;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import no.nsd.qddt.domain.publicationstatus.PublicationStatusJsonListView;
import no.nsd.qddt.domain.publicationstatus.PublicationStatusService;
import no.nsd.qddt.jsonviews.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/publicationstatus")
public class PublicationStatusController {
    
    private PublicationStatusService service;

    @Autowired
    public PublicationStatusController(PublicationStatusService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PublicationStatus get(@PathVariable("id") Long id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public PublicationStatus update(@RequestBody PublicationStatus instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public PublicationStatus create(@RequestBody PublicationStatus instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @JsonView(View.SimpleList.class)
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<PublicationStatusJsonListView> getAll() {

        return service.findAll();
    }
    
}
