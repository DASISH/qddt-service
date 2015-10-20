package no.nsd.qddt.domain.code.web;

import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import no.nsd.qddt.domain.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/code")
public class CodeController {

    private CodeService codeService;

    @Autowired
    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/{TAG}", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Code>> get(@PathVariable("TAG")String tag, Pageable pageable, PagedResourcesAssembler assembler) {

        Page<Code> comments = codeService.findByHashTagPageable(tag, pageable);
        return new ResponseEntity<>(assembler.toResource(comments), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Code get(@PathVariable("id") UUID id) {
        return codeService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Code update(@RequestBody Code code) {
        return codeService.save(code);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Code create(@RequestBody Code code) {
        return codeService.save(code);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        codeService.delete(id);
    }
}
