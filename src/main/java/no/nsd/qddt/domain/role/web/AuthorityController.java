package no.nsd.qddt.domain.role.web;

import no.nsd.qddt.domain.classes.AbstractController;
import no.nsd.qddt.domain.role.Authority;
import no.nsd.qddt.domain.role.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Stig Norland
 */
@RestController
@RequestMapping("/authority")
public class AuthorityController extends AbstractController {

    private final AuthorityService authorityService;


    @Autowired
    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/all", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public Iterable<Authority> getAll() {
        return  authorityService.findAll();
    }




}
