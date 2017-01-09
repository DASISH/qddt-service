package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.domain.responsedomain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */

@RestController
@RequestMapping("/responsedomain")
public class ResponseDomainController {

    private ResponseDomainService responseDomainService;

    @Autowired
    public ResponseDomainController(ResponseDomainService responseDomainService){
        this.responseDomainService = responseDomainService;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseDomainJsonEdit get(@PathVariable("id") UUID id) {
        return responseDomain2Json(responseDomainService.findOne(id));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseDomainJsonEdit update(@RequestBody ResponseDomain responseDomain) {
        return responseDomain2Json(responseDomainService.save(responseDomain));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomainJsonEdit create(@RequestBody ResponseDomain responseDomain) {
        return responseDomain2Json(responseDomainService.save(responseDomain));
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/createmixed" ,method = RequestMethod.GET, params = {"responseDomaindId" ,"missingId" }, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseDomainJsonEdit createMixed(@RequestParam("responseDomaindId") UUID rdId, @RequestParam("missingId") UUID missingId) {

        return responseDomain2Json(responseDomainService.createMixed(rdId,missingId));
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        responseDomainService.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET, params = { "ResponseKind" }, produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ResponseDomainJsonEdit>> getBy(@RequestParam("ResponseKind") ResponseKind response,
                                                                    @RequestParam(value = "Description",defaultValue = "%") String description,
                                                                    @RequestParam(value = "Question",required = false) String question,
                                                                    @RequestParam(value = "Name",defaultValue = "%") String name,
                                                                    Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ResponseDomainJsonEdit> responseDomains = null;
        try {
            if (question == null || question.isEmpty()) {
                responseDomains = responseDomainService.findBy(response, name, description, pageable).map(F->responseDomain2Json(F));
            } else {
                responseDomains = responseDomainService.findByQuestion(response, name, question, pageable).map(F->responseDomain2Json(F));
            }

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return new ResponseEntity<>(assembler.toResource(responseDomains), HttpStatus.OK);
    }

    private ResponseDomainJsonEdit responseDomain2Json(ResponseDomain responseDomain){
        return new ResponseDomainJsonEdit(responseDomain);
    }

}
