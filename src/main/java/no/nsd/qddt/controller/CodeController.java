package no.nsd.qddt.controller;

import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/code")
public class CodeController  extends AbstractAuditController<Code,UUID> {

    @Autowired
    public CodeController(CodeService service){ super(service);}


}
