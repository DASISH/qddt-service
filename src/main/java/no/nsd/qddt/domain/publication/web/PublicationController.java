package no.nsd.qddt.domain.publication.web;

import no.nsd.qddt.domain.publication.Publication;
import no.nsd.qddt.domain.publication.PublicationService;
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
@RequestMapping("/publication")
public class PublicationController {


    private PublicationService service;

    @Autowired
    public PublicationController(PublicationService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Publication get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Publication update(@RequestBody Publication instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Publication create(@RequestBody Publication instance) {
        return service.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Publication>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Publication> selectables = service.findAllPageable(pageable);
        return new ResponseEntity<>(assembler.toResource(selectables), HttpStatus.OK);
    }

//    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
//    public HttpEntity<PagedResources<Publication>>  getBy(@RequestParam(value = "name",defaultValue = "%") String name,
//                                                           @RequestParam(value = "question",defaultValue = "%") String question,
//                                                           Pageable pageable, PagedResourcesAssembler assembler) {
//
//        Page<Publication> questionitems = null;
//        name = name.replace("*","%");
//        question = question.replace("*","%");
//
//        questionitems = service.findByNameLikeAndQuestionLike(name,question, pageable);
//
//        return new ResponseEntity<>(assembler.toResource(questionitems), HttpStatus.OK);
//    }

}