package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import no.nsd.qddt.domain.responsedomain.ResponseKind;
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
    public ResponseDomain get(@PathVariable("id") UUID id) {
        return responseDomainService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseDomain update(@RequestBody ResponseDomain responseDomain) {
        return responseDomainService.save(responseDomain);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomain create(@RequestBody ResponseDomain responseDomain) {
        return responseDomainService.save(responseDomain);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        responseDomainService.delete(id);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET, params = { "ResponseKind" }, produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<ResponseDomain>> getBy(@RequestParam("ResponseKind") String respons,
                                                            @RequestParam(value = "Description",defaultValue = "%") String description,
                                                            @RequestParam(value = "Question",required = false) String question,
                                                            @RequestParam(value = "Name",defaultValue = "%") String name,
                                                            Pageable pageable, PagedResourcesAssembler assembler) {

        Page<ResponseDomain> responseDomains = null;

        if (question == null || question.isEmpty()) {
            System.out.println("findBy(" + respons + ")");
            responseDomains = responseDomainService.findBy(ResponseKind.valueOf(respons), Likeify(name), Likeify(description), pageable);
        } else {
            System.out.println("findByQuestion(" + respons + ")");
            responseDomains = responseDomainService.findByQuestion(ResponseKind.valueOf(respons),  Likeify(name), Likeify(question), pageable);
        }

        System.out.println("RD returned ->" + responseDomains.getTotalElements());
        return new ResponseEntity<>(assembler.toResource(responseDomains), HttpStatus.OK);
    }

    private String Likeify(String value){
        if (!value.startsWith("%"))
            value = "%"+value;
        if (!value.endsWith("%"))
            value = value + "%";
        return value;
    }


}
