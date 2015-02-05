package no.nsd.qddt.controller;

import no.nsd.qddt.controller.audit.BaseAuditController;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping("/instrument")
public class InstrumentController implements BaseAuditController<Instrument> {

    private InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService){
        this.instrumentService = instrumentService;
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Instrument> getAll() {
        return instrumentService.findAll();
    }

    @Override
    @RequestMapping(value = "/psge", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Instrument>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Instrument> instrumentQuestions = instrumentService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instrumentQuestions), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Instrument getOne(@PathVariable("id") Long id) {
        return instrumentService.findById(id);
    }


//    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Instrument getOne(@PathVariable("id") UUID id) {
        return instrumentService.findById(id);
    }

    @Override
    public HttpEntity<PagedResources<Revision<Integer, Instrument>>> getAllRevisionsPageable(Long id, Pageable pageable) {
        return null;
    }


    @Override
    public Revision<Integer, Instrument> getEntityAtRevision(Long id, Integer revision) {
        return null;
    }

    @Override
    public Revision<Integer, Instrument> getLastChange(Long id) {
        return null;
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Instrument create(Instrument instance) {
        return instrumentService.save(instance);
    }


    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Instrument instance) {
        instrumentService.delete(instance);
    }
}
