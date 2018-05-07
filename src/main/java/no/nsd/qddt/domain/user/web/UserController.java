package no.nsd.qddt.domain.user.web;

import no.nsd.qddt.domain.user.IPassword;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.utils.SecurityContext;
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

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public User getLoggedInUser() {
        return userService.findOne(SecurityContext.getUserDetails().getUser().getId());
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<User>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                            Pageable pageable, PagedResourcesAssembler assembler) {

        Page<User> items = userService.getByName(name, pageable);
        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public User update(@RequestBody User instance) {
        return userService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String resetPassword(@RequestBody IPassword instance) {
        return userService.setPassword(instance);
    }


}