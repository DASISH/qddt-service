package no.nsd.qddt.domain.instrumentquestion.web;

import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * This controller relates to a meta storage, which has a rank,logic & Rankrationale property, and thus need control
 *
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


    //    MetaController
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InstrumentQuestion create(InstrumentQuestion comment) {

        return instrumentQuestionService.save(comment);
    }

    //    MetaController
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(InstrumentQuestion instance) {
        instrumentQuestionService.delete(instance.getId());
    }

    //    MetaController
    @RequestMapping(value = "/byInstrument/{uuid}", method = RequestMethod.GET)
    public List<InstrumentQuestion> getByFirst(@PathVariable("uuid") UUID firstId) {
        return instrumentQuestionService.findByInstrumentId(firstId);
    }

    //    MetaController
    @RequestMapping(value = "/byQuestion/{uuid}", method = RequestMethod.GET)
    public List<InstrumentQuestion> getBySecond(@PathVariable("uuid") UUID secondId) {
        return instrumentQuestionService.findByQuestionId(secondId);
    }
}
