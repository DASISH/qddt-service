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
public class InstrumentQuestionController implements BaseMetaController<InstrumentQuestion> {

    private InstrumentQuestionService instrumentQuestionService;

    @Autowired
    public InstrumentQuestionController(InstrumentQuestionService instrumentQuestionService){
        this.instrumentQuestionService = instrumentQuestionService;
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

    @Override
    @RequestMapping(value = "/byInstrument/{id}", method = RequestMethod.GET)
    public List<InstrumentQuestion> getByFirst(@PathVariable("id") Long firstId) {
        return instrumentQuestionService.findByInstrumentId(firstId);
    }

    @Override
    @RequestMapping(value = "/byQuestion/{id}", method = RequestMethod.GET)
    public List<InstrumentQuestion> getBySecond(@PathVariable("id") Long secondId) {
        return instrumentQuestionService.findByQuestionId(secondId);
    }
}
