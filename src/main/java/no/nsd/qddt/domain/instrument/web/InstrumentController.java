package no.nsd.qddt.domain.instrument.web;

import no.nsd.qddt.domain.instrument.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping("/instrument")
public class InstrumentController  {

    private
    InstrumentService instrumentService;

    @Autowired
    public InstrumentController(InstrumentService instrumentService){
        this.instrumentService = instrumentService;
    }

}
