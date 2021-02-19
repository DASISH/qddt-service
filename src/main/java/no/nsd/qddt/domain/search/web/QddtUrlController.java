package no.nsd.qddt.domain.search.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.search.QddtUrl;
import no.nsd.qddt.domain.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/preview")
public class QddtUrlController extends AbstractController {

    private final SearchService service;

    @Autowired
    public QddtUrlController(SearchService service) {
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public QddtUrl get(@PathVariable("id") UUID id) {

        return service.findPath( id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{id}/{revision}", method = RequestMethod.GET)
    public QddtUrl getByRevision(@PathVariable("id") UUID id, @PathVariable("revision") Integer revision) {
        QddtUrl url = service.findPath( id);
        url.setRevision( revision );
        return url;
    }
}
