package no.nsd.qddt.domain.responsedomain.web;

import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
