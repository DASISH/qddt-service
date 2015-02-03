package no.nsd.qddt.controller;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.service.ResponseDomainCodeService;
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
@RequestMapping("/responseDomainCode")
public class ResponseDomainCodeController implements BaseController<ResponseDomainCode> {

    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    public ResponseDomainCodeController(ResponseDomainCodeService responseDomainCodeService){
        this.responseDomainCodeService = responseDomainCodeService;
    }


    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ResponseDomainCode> getAll() {
        return responseDomainCodeService.findAll();
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<ResponseDomainCode>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ResponseDomainCode> instances = responseDomainCodeService.findAll( pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }


    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseDomainCode getOne(@PathVariable("id") Long id) {

        //TODO denne skal kanskje ikke brukes?
        return responseDomainCodeService.findById(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseDomainCode getOne(@PathVariable("id") UUID id) {
        return responseDomainCodeService.findById(id);
    }


    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomainCode create(ResponseDomainCode responseDomainCode) {

        return responseDomainCodeService.save(responseDomainCode);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(ResponseDomainCode instance) {
        responseDomainCodeService.delete(instance);
    }
}
