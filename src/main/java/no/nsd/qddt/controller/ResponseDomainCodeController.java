package no.nsd.qddt.controller;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.service.ResponseDomainCodeService;
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
@RequestMapping("/responseDomainCode")
public class ResponseDomainCodeController {

    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    public ResponseDomainCodeController(ResponseDomainCodeService responseDomainCodeService){
        this.responseDomainCodeService = responseDomainCodeService;
    }

    //    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET)
//    public HttpEntity<PagedResources<InstrumentQuestion>> getThread(
//            @PathVariable("id") Long id,Pageable pageable, PagedResourcesAssembler assembler)
//    {
//        Page<InstrumentQuestion> instrumentQuestions = instrumentQuestionService (id, pageable);
//        return new ResponseEntity<>(assembler.toResource(instrumentQuestions), HttpStatus.OK);
//    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ResponseDomainCode> getAll() {
        return responseDomainCodeService.findAll();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseDomainCode getOne(@PathVariable("id") Long id) {

        //TODO denne skal kanskje ikke brukes?
        return null;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomainCode create(ResponseDomainCode responseDomainCode) {

        return responseDomainCodeService.save(responseDomainCode);
    }
}
