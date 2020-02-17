package no.nsd.qddt.domain.referenced.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.referenced.Referenced;
import no.nsd.qddt.domain.referenced.ReferencedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/references")
public class ReferencedController extends AbstractController {

    private final ReferencedService service;

    @Autowired
    public ReferencedController(ReferencedService service) {
        this.service = service;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Referenced>> getPage(@RequestParam(value = "kind",defaultValue = "%") String kind,
                                                            Pageable pageable, PagedResourcesAssembler assembler) {
        return new ResponseEntity<>(assembler.toResource(service.findAll(kind, pageable)), HttpStatus.OK);
    }


}
