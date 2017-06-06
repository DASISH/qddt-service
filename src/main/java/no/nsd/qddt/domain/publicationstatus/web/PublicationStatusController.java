package no.nsd.qddt.domain.publicationstatus.web;

import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import no.nsd.qddt.domain.publicationstatus.json.PublicationStatusJsonParent;
import no.nsd.qddt.domain.publicationstatus.PublicationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list", method = RequestMethod.GET , produces = "application/json")
    public List<PublicationStatusJsonParent> getAll() {
        List<PublicationStatusJsonParent> list =service.findAll()
                .stream().map(s->new PublicationStatusJsonParent(s))
                .sorted(Comparator.comparing(PublicationStatusJsonParent::getChildrenIdx))
                .collect(Collectors.toList());
        return list;
    }
    
}
