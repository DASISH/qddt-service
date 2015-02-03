package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.service.InstrumentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/instrumentQuestion")
public class InstrumentQuestionController implements BaseController<InstrumentQuestion> {

    private InstrumentQuestionService instrumentQuestionService;

    @Autowired
    public InstrumentQuestionController(InstrumentQuestionService instrumentQuestionService){
        this.instrumentQuestionService = instrumentQuestionService;
    }

//    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<InstrumentQuestion>> getThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//        Page<InstrumentQuestion> instrumentQuestions = instrumentQuestionService (id, pageable);
//        return new ResponseEntity<>(assembler.toResource(instrumentQuestions), HttpStatus.OK);
//    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<InstrumentQuestion> getAll() {
        return instrumentQuestionService.findAll();
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<InstrumentQuestion>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<InstrumentQuestion> instances = instrumentQuestionService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InstrumentQuestion getOne(@PathVariable("id") Long id) {

        return instrumentQuestionService.findById(id);
    }

    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public InstrumentQuestion getOne(@PathVariable("id") UUID id) {
        return instrumentQuestionService.findById(id);
    }

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InstrumentQuestion create(InstrumentQuestion comment) {

        return instrumentQuestionService.save(comment);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(InstrumentQuestion instance) {
        instrumentQuestionService.delete(instance);
    }
}
