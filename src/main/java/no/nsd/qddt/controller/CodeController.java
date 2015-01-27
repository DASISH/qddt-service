package no.nsd.qddt.controller;

import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.service.CodeService;
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
@RequestMapping("/code")
public class CodeController {

    private CodeService codeService;

    @Autowired
    public CodeController(CodeService codeService){
        this.codeService = codeService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Code> getAll() {
        return codeService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Code getOne(@PathVariable("id") Long id) {return codeService.findById(id);}

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Code create(Code agency) {return codeService.save(agency);}

}
