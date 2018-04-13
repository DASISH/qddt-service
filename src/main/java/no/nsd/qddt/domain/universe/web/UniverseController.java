package no.nsd.qddt.domain.universe.web;

import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.universe.UniverseJsonView;
import no.nsd.qddt.domain.universe.UniverseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@RequestMapping("/universe")
public class UniverseController {

    private final UniverseService service;

    @Autowired
    public UniverseController(UniverseService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Universe get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Universe update(@RequestBody Universe universe) {
        return service.save(universe);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Universe create(@RequestBody Universe universe) {
        return service.save(universe);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }



    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<UniverseJsonView>> getBy(@RequestParam(value = "description",defaultValue = "%") String description,
                                                              Pageable pageable, PagedResourcesAssembler assembler) {

        Page<UniverseJsonView> universes = service.findByDescriptionLike(description, pageable).map( u -> new UniverseJsonView( u ) );
        return new ResponseEntity<>(assembler.toResource(universes), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }
}

