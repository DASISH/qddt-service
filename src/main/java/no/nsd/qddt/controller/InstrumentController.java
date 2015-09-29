package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping("/instrument")
public class InstrumentController extends AbstractAuditController<Instrument,UUID> {

    @Autowired
    public InstrumentController(InstrumentService instrumentService){
        super(instrumentService);
    }

}
