package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
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
public class InstrumentController implements BaseController<Instrument> {

    private InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService){
        this.instrumentService = instrumentService;
    }

    //    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<InstrumentQuestion>> getThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//        Page<InstrumentQuestion> instrumentQuestions = instrumentQuestionService (id, pageable);
//        return new ResponseEntity<>(assembler.toResource(instrumentQuestions), HttpStatus.OK);
//    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Instrument> getAll() {
        return instrumentService.findAll();
    }

    @Override
    public HttpEntity<PagedResources<Instrument>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        return null;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Instrument getOne(@PathVariable("id") Long id) {

        return instrumentService.findById(id);
    }

    @Override
    public Instrument getOne(UUID id) {
        return null;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Instrument create(Instrument comment) {

        return instrumentService.save(comment);
    }

    @Override
    public void delete(Instrument instance) {

    }
}
