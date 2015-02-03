package no.nsd.qddt.controller;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/code")
public class CodeController  implements BaseController<Code> {

    private CodeService codeService;

    @Autowired
    public CodeController(CodeService codeService){
        this.codeService = codeService;
    }


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Code> getAll() {
        return codeService.findAll();
    }


    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Code>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Code> instances = codeService.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Code getOne(@PathVariable("id") Long id) {return codeService.findById(id);}


    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public Code getOne(@PathVariable("id") UUID id) {
        return codeService.findById(id);
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Code create(Code agency) {return codeService.save(agency);}


    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(Code instance) {
        codeService.delete(instance);
    }

}
