package no.nsd.qddt.controller;

import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/code")
public class CodeController  extends AbstractAuditController<Code> {

    @Autowired
    public CodeController(CodeService service){ super(service);}


}
