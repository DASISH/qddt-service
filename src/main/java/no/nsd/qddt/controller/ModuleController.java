package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Module;
import no.nsd.qddt.service.ModuleService;
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
@RequestMapping("/module")
public class ModuleController {
    private ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Module> getAll() {
        return moduleService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Module getOne(@PathVariable("id") Long id) {
        return moduleService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Module create(Module module) {
        return moduleService.save(module);
    }
}
