package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.responsedomain.*;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonEdit;
import no.nsd.qddt.exception.RequestAbortedException;
import no.nsd.qddt.exception.StackTraceFilter;
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

import javax.validation.ConstraintViolationException;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */

@RestController
@RequestMapping("/responsedomain")
public class ResponseDomainController {

    private final ResponseDomainService service;
    private final CategoryService categoryService;

    @Autowired
    public ResponseDomainController(ResponseDomainService service,CategoryService categoryService){
        this.service = service;
        this.categoryService = categoryService;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseDomain get(@PathVariable("id") UUID id) {
        return service.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseDomain update(@RequestBody ResponseDomain responseDomain) {
        return service.save(responseDomain);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomain create(@RequestBody ResponseDomain responseDomain) {
        assert  responseDomain != null;
        responseDomain = service.save(responseDomain);
        //HACK -> after saving responsdomain, save managed representation one more time to correct name and version...
//        categoryService.save(responseDomain.getManagedRepresentation());
        return responseDomain;
    }

//    @ResponseStatus(value = HttpStatus.CREATED)
//    @RequestMapping(value = "/createmixed" ,method = RequestMethod.GET, params = {"responseDomaindId" ,"missingId" }, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseDomainJsonEdit createMixed(@RequestParam("responseDomaindId") UUID rdId, @RequestParam("missingId") UUID missingId) {
//
//        return responseDomain2Json(service.createMixed(rdId,missingId));
//    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) throws RequestAbortedException {
        try {
            service.delete(id);
        }catch (ConstraintViolationException cex){
            throw new RequestAbortedException(cex);
        }catch (Exception ex){
            throw new RequestAbortedException("This ResponseDomain is referenced and cannot be deleted.");
        }
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
                responseDomains = service.findBy(response, name, description, pageable).map(this::responseDomain2Json);
            } else {
                responseDomains = service.findByQuestion(response,  question, pageable).map(this::responseDomain2Json);
            }

        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }

        return new ResponseEntity<>(assembler.toResource(responseDomains), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/xml/{id}", method = RequestMethod.GET)
    public String getXml(@PathVariable("id") UUID id) {
        return service.findOne(id).toDDIXml();
    }

    private ResponseDomainJsonEdit responseDomain2Json(ResponseDomain responseDomain){
        return new ResponseDomainJsonEdit(responseDomain);
    }

}
