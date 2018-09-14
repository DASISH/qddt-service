package no.nsd.qddt.domain.user.web;

import no.nsd.qddt.domain.user.Password;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.domain.user.json.UserJsonEdit;
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

import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

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


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User get(@PathVariable("id") UUID id) {
        return userService.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public User getLoggedInUser() {
        return userService.findOne(SecurityContext.getUserDetails().getUser().getId());
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/page/search", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public HttpEntity<PagedResources<UserJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                            Pageable pageable, PagedResourcesAssembler assembler) {
        pageable = defaultOrModifiedSort(pageable, "name ASC", "updated DESC");
        Page<UserJsonEdit> items = userService.getByName(name, pageable).map( c -> new UserJsonEdit(c) );
        return new ResponseEntity<>(assembler.toResource(items), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        userService.delete(id);
    }


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE)
    public User update(@RequestBody User instance) {

        return  userService.save(instance);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String resetPassword(@RequestBody Password instance) {
        return userService.setPassword(instance);
    }



}