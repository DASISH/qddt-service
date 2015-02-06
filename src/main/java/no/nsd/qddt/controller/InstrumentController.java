package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping("/instrument")
public class InstrumentController extends AbstractAuditController<Instrument> {

    @Autowired
    public InstrumentController(InstrumentService instrumentService){
        super(instrumentService);
    }

}
