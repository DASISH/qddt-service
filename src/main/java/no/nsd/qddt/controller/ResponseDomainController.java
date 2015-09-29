package no.nsd.qddt.controller;

import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.responsedomain.ResponseDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */

@RestController
@RequestMapping("/responseDomain")
public class ResponseDomainController extends AbstractAuditController<ResponseDomain,UUID> {

    @Autowired
    public ResponseDomainController(ResponseDomainService responseDomainService){
        super(responseDomainService);
    }

}
