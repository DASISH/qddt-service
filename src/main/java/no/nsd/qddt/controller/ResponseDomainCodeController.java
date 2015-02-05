package no.nsd.qddt.controller;

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
public class ResponseDomainCodeController implements BaseMetaController<ResponseDomainCode> {

    private ResponseDomainCodeService responseDomainCodeService;

    @Autowired
    public ResponseDomainCodeController(ResponseDomainCodeService responseDomainCodeService){
        this.responseDomainCodeService = responseDomainCodeService;
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

    @Override
    @RequestMapping(value = "/byResponsDomain/{id}", method = RequestMethod.GET)
    public List<ResponseDomainCode> getByFirst(@PathVariable("id") Long firstId) {
        return responseDomainCodeService.findByResponseDomainId(firstId);
    }

    @Override
    @RequestMapping(value = "/byCode/{id}", method = RequestMethod.GET)
    public List<ResponseDomainCode> getBySecond(@PathVariable("id") Long secondId) {
        return responseDomainCodeService.findByCodeId(secondId);
    }
}
