package no.nsd.qddt.domain.code.web;

import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * This controller relates to a meta storage, which has a rank property, and thus need control
 *
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


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Code get(@PathVariable("id") UUID id) {
        return codeService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Code update(@RequestBody Code instance) {
        return codeService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Code create(@RequestBody Code instance) {
        return codeService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        codeService.delete(id);
    }

    //    MetaController
    @RequestMapping(value = "/list/by-responsedomain/{uuid}", method = RequestMethod.GET)
    public List<Code> getByFirst(@PathVariable("uuid") UUID firstId) {
        return codeService.findByResponseDomainId(firstId);
    }

    //    MetaController
    @RequestMapping(value = "/list/by-code/{uuid}", method = RequestMethod.GET)
    public List<Code> getBySecond(@PathVariable("uuid") UUID secondId) {
        return codeService.findByCategoryId(secondId);
    }
}
