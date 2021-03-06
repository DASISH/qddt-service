package no.nsd.qddt.domain.publication.web;

import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;
import no.nsd.qddt.domain.publication.Publication;
import no.nsd.qddt.domain.publication.PublicationJson;
import no.nsd.qddt.domain.publication.PublicationService;
import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentAssembler;
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
 * @author Stig Norland
 */
@RestController
@RequestMapping("/publication")
public class PublicationController {

    private final PublicationService service;

    @Autowired
    public PublicationController(PublicationService service) {
        this.service = service;
    }

    // // @JsonView(View.Simple.class)
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Publication get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/element/", method = RequestMethod.POST)
    public ElementRefEmbedded getDetail(@RequestBody ElementRefEmbedded instance) {
        return service.getDetail(instance);
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
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public PagedModel<EntityModel<PublicationJson>> getBy(@RequestParam(value = "name", defaultValue = "") String name,
                                                          @RequestParam(value = "purpose", defaultValue = "") String purpose,
                                                          @RequestParam(value = "publicationStatus", defaultValue = "") String publicationStatus,
                                                          @RequestParam(value = "publishedKind") String publishedKind,
                                                          Pageable pageable, PagedResourcesAssembler<PublicationJson> assembler) {

        Page<PublicationJson> items =
                service.findByNameOrPurposeAndStatus(name, purpose, publicationStatus, publishedKind, pageable)
                .map( c -> new PublicationJson(c));

        return assembler.toModel(items);
    }


    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return new XmlDDIFragmentAssembler<Publication>(service.findOne(id)).compileToXml();

    }
}
