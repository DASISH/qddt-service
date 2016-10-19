package no.nsd.qddt.domain.controlconstruct.web;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.ControlConstructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * This controller relates to a meta storage, which has a rank,logic and Rankrationale property, and thus need control
 *
 * @author Stig Norland
 */

@RestController
@RequestMapping("/controlconstruct")
public class ControlConstructController {

    private ControlConstructService controlConstructService;

    @Autowired
    public ControlConstructController(ControlConstructService controlConstructService){
        this.controlConstructService = controlConstructService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ControlConstruct get(@PathVariable("id") UUID id) {

        ControlConstruct cc= controlConstructService.findOne(id);
        cc.populateInstructions();
        return cc;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ControlConstruct update(@RequestBody ControlConstruct instance) {

        instance.populateControlConstructs();
        return controlConstructService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ControlConstruct create(@RequestBody ControlConstruct instance) {

        instance.populateControlConstructs();
        return controlConstructService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        controlConstructService.delete(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-instrument/{uuid}", method = RequestMethod.GET)
    public List<ControlConstruct> getByFirst(@PathVariable("uuid") UUID firstId) {

        List<ControlConstruct> ccs =controlConstructService.findByInstrumentId(firstId);
        ccs.forEach(cc->cc.populateInstructions());
        return ccs;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/list/by-question/{uuid}", method = RequestMethod.GET)
    public List<ControlConstruct> getBySecond(@PathVariable("uuid") UUID secondId) {

        List<ControlConstruct> ccs =controlConstructService.findByQuestionItemId(secondId);
        ccs.forEach(cc->cc.populateInstructions());
        return ccs;
    }
}
