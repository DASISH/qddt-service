package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.service.ResponseDomainService;
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
@RequestMapping("/responseDomain")
public class ResponseDomainController {

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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ResponseDomain> getAll() {
        return responseDomainService.findAll();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseDomain getOne(@PathVariable("id") Long id) {

        return responseDomainService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomain create(ResponseDomain responseDomain) {

        return responseDomainService.save(responseDomain);
    }
}
