package no.nsd.qddt.controller;

import no.nsd.qddt.controller.audit.BaseAuditController;
import no.nsd.qddt.domain.Module;
import no.nsd.qddt.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/module")
public class ModuleController implements BaseAuditController<Module> {

    private ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Module> getAll() {
        return moduleService.findAll();
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Module>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Module> instances = moduleService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Module getOne(@PathVariable("id") Long id) {
        return moduleService.findById(id);
    }


    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Module getOne(@PathVariable("id") UUID id) {
        return moduleService.findById(id);
    }

    @Override
    public HttpEntity<PagedResources<Revision<Integer, Module>>> getAllRevisionsPageable(Long id, Pageable pageable) {
        return null;
    }

    @Override
    public Revision<Integer, Module> getEntityAtRevision(Long id, Integer revision) {
        return null;
    }

    @Override
    public Revision<Integer, Module> getLastChange(Long id) {
        return null;
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Module create(Module module) {
        return moduleService.save(module);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Module instance) {
        moduleService.delete(instance);
    }

}
