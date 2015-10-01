package no.nsd.qddt.domain.responsedomaincode.web;

import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


//    MetaController
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDomainCode create(ResponseDomainCode responseDomainCode) {

        return responseDomainCodeService.save(responseDomainCode);
    }

    //    MetaController
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(ResponseDomainCode instance) {
        responseDomainCodeService.delete(instance.getId());
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
