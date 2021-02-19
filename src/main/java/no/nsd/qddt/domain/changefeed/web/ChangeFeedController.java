package no.nsd.qddt.domain.changefeed.web;

import no.nsd.qddt.domain.AbstractController;
import no.nsd.qddt.domain.changefeed.ChangeFeed;
import no.nsd.qddt.domain.changefeed.ChangeFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping(value = "/changelog")
public class ChangeFeedController  extends AbstractController {

    private final ChangeFeedService service;

    @Autowired
    public ChangeFeedController(ChangeFeedService service) {
        this.service = service;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public PagedModel<EntityModel<ChangeFeed>> getPage(
        @RequestParam(value = "name",defaultValue = "%") String name,
        @RequestParam(value = "change",defaultValue = "%") String change,
        @RequestParam(value = "kind",defaultValue = "%") String kind,
        Pageable pageable, PagedResourcesAssembler<ChangeFeed> assembler) {

        return assembler.toModel(
            service.filterbyPageable(name,change,kind, pageable)
        );
    }


}
