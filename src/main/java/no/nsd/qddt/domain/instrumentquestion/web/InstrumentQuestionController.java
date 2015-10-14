package no.nsd.qddt.domain.instrumentquestion.web;

import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * This controller relates to a meta storage, which has a rank,logic & Rankrationale property, and thus need control
 *
 * @author Stig Norland
 */

@RestController
@RequestMapping("/instrumentquestion")
public class InstrumentQuestionController {

    private InstrumentQuestionService instrumentQuestionService;

    @Autowired
    public InstrumentQuestionController(InstrumentQuestionService instrumentQuestionService){
        this.instrumentQuestionService = instrumentQuestionService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public InstrumentQuestion get(@PathVariable("id") UUID id) {
        return instrumentQuestionService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InstrumentQuestion update(@RequestBody InstrumentQuestion instance) {
        return instrumentQuestionService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InstrumentQuestion create(@RequestBody InstrumentQuestion instance) {
        return instrumentQuestionService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        instrumentQuestionService.delete(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/byInstrument/{uuid}", method = RequestMethod.GET)
    public List<InstrumentQuestion> getByFirst(@PathVariable("uuid") UUID firstId) {
        return instrumentQuestionService.findByInstrumentId(firstId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/byQuestion/{uuid}", method = RequestMethod.GET)
    public List<InstrumentQuestion> getBySecond(@PathVariable("uuid") UUID secondId) {
        return instrumentQuestionService.findByQuestionId(secondId);
    }
}
