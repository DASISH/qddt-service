package no.nsd.qddt.domain.user.web;

import no.nsd.qddt.domain.security.SecurityContext;
import no.nsd.qddt.domain.user.Password;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.domain.user.json.UserJsonEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/{id}")
    public User get(@PathVariable("id") UUID id) {
        return userService.findOne(id);
    }

    @GetMapping("/user")
    public User getLoggedInUser() {
        return userService.findOne(SecurityContext.getUserDetails().getUser().getId());
    }

    @GetMapping("/user/page/search")
    public PagedModel<EntityModel<UserJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                       Pageable pageable, PagedResourcesAssembler<UserJsonEdit> assembler) {
        pageable = defaultOrModifiedSort(pageable, "name ASC");
        Page<UserJsonEdit> items = userService.getByName(name, pageable).map( UserJsonEdit::new );
        return assembler.toModel(items);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") UUID id) {
        userService.delete(id);
    }


    @PostMapping( "/user")
    public User update(@Valid @RequestBody User instance) {

        return  userService.save(instance);
    }

    @PatchMapping("/user/resetpassword")
    public String resetPassword(@RequestBody Password instance) {
        return userService.setPassword(instance);
    }



}
