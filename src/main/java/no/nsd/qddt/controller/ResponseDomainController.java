package no.nsd.qddt.controller;

import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.service.ResponseDomainService;
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
@RequestMapping("/responseDomain")
public class ResponseDomainController implements BaseController<ResponseDomain> {

    private ResponseDomainService responseDomainService;

    @Autowired
    public ResponseDomainController(ResponseDomainService responseDomainService){
        this.responseDomainService = responseDomainService;
    }

    //    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<InstrumentQuestion>> getThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//        Page<InstrumentQuestion> instrumentQuestions = instrumentQuestionService (id, pageable);
//        return new ResponseEntity<>(assembler.toResource(instrumentQuestions), HttpStatus.OK);
//    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ResponseDomain> getAll() {
        return responseDomainService.findAll();
    }

    @Override
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public HttpEntity<PagedResources<ResponseDomain>> getAll(Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ResponseDomain> instances = responseDomainService.findAll( pageable);
        return new ResponseEntity<>(assembler.toResource(instances), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseDomain getOne(@PathVariable("id") Long id) {
        return responseDomainService.findById(id);
    }

    @Override
    @RequestMapping(value = "/UUID/{id}", method = RequestMethod.GET)
    public ResponseDomain getOne(@PathVariable("id") UUID id) {
        return responseDomainService.findById(id);
    }

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomain create(ResponseDomain responseDomain) {
        return responseDomainService.save(responseDomain);
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(ResponseDomain instance) {
        responseDomainService.delete(instance);
    }
}
