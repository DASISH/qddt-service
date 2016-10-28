package no.nsd.qddt.domain.selectable.web;

import no.nsd.qddt.domain.selectable.Selectable;
import no.nsd.qddt.domain.selectable.SelectableService;
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
 * @author Stig Norland
 */
@RestController
@RequestMapping("/selectable")
public class SelectableController {


    private SelectableService service;

    @Autowired
    public SelectableController(SelectableService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Selectable get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Selectable update(@RequestBody Selectable instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Selectable create(@RequestBody Selectable instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Selectable>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Selectable> selectables = service.findAllPageable(pageable);
        return new ResponseEntity<>(assembler.toResource(selectables), HttpStatus.OK);
    }

//    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
//    public HttpEntity<PagedResources<Selectable>>  getBy(@RequestParam(value = "name",defaultValue = "%") String name,
//                                                           @RequestParam(value = "question",defaultValue = "%") String question,
//                                                           Pageable pageable, PagedResourcesAssembler assembler) {
//
//        Page<Selectable> questionitems = null;
//        name = name.replace("*","%");
//        question = question.replace("*","%");
//
//        questionitems = service.findByNameLikeAndQuestionLike(name,question, pageable);
//
//        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
//    }

}