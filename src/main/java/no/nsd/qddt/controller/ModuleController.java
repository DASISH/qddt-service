package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Module;
import no.nsd.qddt.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/module")
public class ModuleController extends AbstractAuditController<Module> {


    @Autowired
    public ModuleController(ModuleService moduleService) {
        super(moduleService);
    }

}
