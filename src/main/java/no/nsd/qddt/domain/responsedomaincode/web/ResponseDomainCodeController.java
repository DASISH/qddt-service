package no.nsd.qddt.domain.responsedomaincode.web;

import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeService;
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
@RequestMapping("/responseDomainCode")
public class ResponseDomainCodeController  {

    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    public ResponseDomainCodeController(ResponseDomainCodeService responseDomainCodeService){
        this.responseDomainCodeService = responseDomainCodeService;
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseDomainCode get(@PathVariable("id") UUID id) {
        return responseDomainCodeService.findOne(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseDomainCode update(@RequestBody ResponseDomainCode instance) {
        return responseDomainCodeService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomainCode create(@RequestBody ResponseDomainCode instance) {
        return responseDomainCodeService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") UUID id) {
        responseDomainCodeService.delete(id);
    }

    //    MetaController
    @RequestMapping(value = "/byResponsDomain/{id}", method = RequestMethod.GET)
    public List<ResponseDomainCode> getByFirst(@PathVariable("id") UUID firstId) {
        return responseDomainCodeService.findByResponseDomainId(firstId);
    }

    //    MetaController
    @RequestMapping(value = "/byCode/{id}", method = RequestMethod.GET)
    public List<ResponseDomainCode> getBySecond(@PathVariable("id") UUID secondId) {
        return responseDomainCodeService.findByCodeId(secondId);
    }
}
