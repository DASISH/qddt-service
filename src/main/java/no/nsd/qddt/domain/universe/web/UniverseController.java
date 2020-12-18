package no.nsd.qddt.domain.universe.web;

import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.universe.UniverseJsonEdit;
import no.nsd.qddt.domain.universe.UniverseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public UniverseJsonEdit get(@PathVariable("id") UUID id) {
        return new UniverseJsonEdit(service.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public UniverseJsonEdit update(@RequestBody Universe universe) {
        return new UniverseJsonEdit(service.save(universe));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UniverseJsonEdit create(@RequestBody Universe universe) {
        return new UniverseJsonEdit(service.save(universe));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }



    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public PagedModel<EntityModel<UniverseJsonEdit>> getBy(@RequestParam(value = "description",defaultValue = "%") String description,
                                                           @RequestParam(value = "xmlLang",defaultValue = "") String xmlLang,
                                                           Pageable pageable, PagedResourcesAssembler<UniverseJsonEdit> assembler) {

        Page<UniverseJsonEdit> universes = service.findByDescriptionLike(description, xmlLang, pageable)
            .map( UniverseJsonEdit::new );
        return assembler.toModel(universes);
    }


}

