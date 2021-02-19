package no.nsd.qddt.domain.user.web;

import no.nsd.qddt.domain.classes.exception.ReferenceInUseException;
import no.nsd.qddt.domain.search.SearchService;
import no.nsd.qddt.domain.user.Password;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.domain.user.json.UserJsonEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@RestController
@Validated
public class UserController {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SearchService searchService;
    private final UserService userService;

    @Autowired
    public UserController(SearchService searchService, UserService userService) {
        this.searchService = searchService;
        this.userService = userService;
    }



    @GetMapping("/user/{id}")
    public User get(@PathVariable("id") UUID id) {
        return userService.findOne(id);
    }

    @GetMapping("/user")
    public User getLoggedInUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userService.findOne( user.getId());
    }

    @GetMapping("/user/page/search")
    public PagedModel<EntityModel<UserJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                       Pageable pageable, PagedResourcesAssembler<UserJsonEdit> assembler) {
        pageable = defaultOrModifiedSort(pageable, "name ASC");
        Page<UserJsonEdit> items = userService.findByName(name, pageable).map( UserJsonEdit::new );
        return assembler.toModel(items);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") UUID id) {
        if (searchService.findByUserId(id).size() > 0)
            throw new ReferenceInUseException( id.toString() );
        userService.delete(id);
    }


    @PostMapping( "/user")
    public User update(@Valid @RequestBody User instance) {
        return  userService.save(instance);
    }

    @PatchMapping("/user/resetpassword")
    public void resetPassword(@RequestBody Password instance) {
        userService.setPassword(  instance );
    }


//    private User prePersistProcessing(User instance) {
//        if (instance.getAuthorities().size() == 0) {
//            instance.setAuthorities(authorityService.findAll()
//                .stream().filter(i->i.getAuthority() == "ROLE_LIMITED")
//                .collect( Collectors.toSet()));
//        }
//        if (instance.getId() == null) {
//            instance.setPassword( "$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW" );
//        }
//        return instance;
//    }
}
