package no.nsd.qddt.domain.user.web;

import no.nsd.qddt.classes.exception.ReferenceInUseException;
import no.nsd.qddt.classes.exception.UserNotFoundException;
import no.nsd.qddt.domain.role.AuthorityService;
import no.nsd.qddt.domain.search.SearchService;
import no.nsd.qddt.domain.user.Password;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.UserRepository;
import no.nsd.qddt.domain.user.json.UserJsonEdit;
import no.nsd.qddt.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.configuration.SecurityConfiguration.passwordEncoderBean;
import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@RestController
@Validated
public class UserController {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final SearchService searchService;

    @Autowired
    public UserController(UserRepository userRepository,AuthorityService authorityService,SearchService searchService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.searchService = searchService;
    }

    @GetMapping("/user/{id}")
    public User get(@PathVariable("id") UUID id) {
        return userRepository.getOne(id);
    }

    @GetMapping("/user")
    public User getLoggedInUser() {
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getOne( userDetails.getId());
    }

    @GetMapping("/user/page/search")
    public PagedModel<EntityModel<UserJsonEdit>> getBy(@RequestParam(value = "name",defaultValue = "%") String name,
                                                       Pageable pageable, PagedResourcesAssembler<UserJsonEdit> assembler) {
        pageable = defaultOrModifiedSort(pageable, "name ASC");
        Page<UserJsonEdit> items = userRepository.findByUsernameIgnoreCaseLike(name, pageable).map( UserJsonEdit::new );
        return assembler.toModel(items);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") UUID uuid) {
        if (searchService.findByUserId(uuid).size() > 0)
            throw new ReferenceInUseException( uuid.toString() );
        userRepository.deleteById(uuid);
    }


    @PostMapping( "/user")
    public User update(@Valid @RequestBody User instance) {
        return  userRepository.save(instance);
    }

    @PatchMapping("/user/resetpassword")
    public void resetPassword(@RequestBody Password instance) {

        User user = userRepository.findById( instance.getId() )
            .orElseThrow(  () -> new UserNotFoundException(instance.getId()) );

        if (!passwordEncoderBean().matches( instance.getOldPassword(), user.getPassword() )) {
            throw new OAuth2AuthenticationException(new OAuth2Error( OAuth2ErrorCodes.INVALID_REQUEST), "Password mismatch ()" );
        }

        userRepository.setPassword( user.getId(), passwordEncoderBean().encode( instance.getPassword() ) );
    }


    private User prePersistProcessing(User instance) {
        if (instance.getAuthorities().size() == 0) {
            instance.setAuthorities(authorityService.findAll()
                .stream().filter(i->i.getAuthority() == "ROLE_LIMITED")
                .collect( Collectors.toSet()));
        }
        if (instance.getId() == null) {
            instance.setPassword( "$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW" );
        }
        return instance;
    }
}
