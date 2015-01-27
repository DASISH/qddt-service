package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.service.InstrumentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping("/instrumentQuestion")
public class InstrumentQuestionController {

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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<InstrumentQuestion> getAll() {
        return instrumentQuestionService.findAll();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InstrumentQuestion getOne(@PathVariable("id") Long id) {

        return instrumentQuestionService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InstrumentQuestion create(InstrumentQuestion comment) {

        return instrumentQuestionService.save(comment);
    }
}
