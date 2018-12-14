package no.nsd.qddt.domain.instrument.web;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentViewJson;
import no.nsd.qddt.domain.instrument.InstrumentService;
import no.nsd.qddt.domain.xml.XmlDDIFragmentAssembler;
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
 * @author Dag Østgulen Heradstveit
 */

@RestController
@RequestMapping("/instrument")
public class InstrumentController  {

    private final InstrumentService service;

    @Autowired
    public InstrumentController(InstrumentService service){
        this.service = service;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Instrument get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }
    

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Instrument update(@RequestBody Instrument instrument) {
        return service.save(instrument);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Instrument create(@RequestBody Instrument instrument) {
        return service.save(instrument);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<Instrument>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Instrument> instruments = service.findAllPageable(pageable);
        return new ResponseEntity<>(assembler.toResource(instruments), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<InstrumentViewJson>> getBy(@RequestParam(value = "name",defaultValue = "") String name,
                                                                @RequestParam(value = "description",defaultValue = "") String decsription,
                                                                @RequestParam(value = "kind",defaultValue = "") String strKind,
                                                                Pageable pageable, PagedResourcesAssembler assembler) {

        Page<InstrumentViewJson> items = service.findByNameAndDescriptionPageable(name,decsription,strKind, pageable)
                                            .map(c -> new InstrumentViewJson(c));

        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public byte[] getPdf(@PathVariable("id") UUID id) {
        return service.findOne(id).makePdf().toByteArray();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return new XmlDDIFragmentAssembler<Instrument>(service.findOne(id)).compileToXml();
    }

}
