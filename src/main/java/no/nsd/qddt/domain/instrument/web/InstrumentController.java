package no.nsd.qddt.domain.instrument.web;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@RestController
@RequestMapping("/instrument")
public class InstrumentController  {

    private InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService){
        this.instrumentService = instrumentService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Instrument get(@PathVariable("id") UUID id) {
        return instrumentService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public Instrument update(@RequestBody Instrument instrument) {
        return instrumentService.save(instrument);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Instrument create(@RequestBody Instrument instrument) {
        return instrumentService.save(instrument);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        instrumentService.delete(id);
    }

}
