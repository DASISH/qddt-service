package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.role.AuthorityService;
import no.nsd.qddt.domain.search.SearchService;
import no.nsd.qddt.exception.ReferenceInUseException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.configuration.SecurityConfiguration.passwordEncoder;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Service("userService")
class UserServiceImpl implements UserService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final SearchService searchService;

    @Autowired
    public UserServiceImpl(
        UserRepository userRepository
        ,AuthorityService authorityService
        ,SearchService searchService) {

        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.searchService = searchService;
    }


    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return userRepository.existsById(uuid);
    }

    @Override
    public User findOne(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, User.class)
        );
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasPermission('OWNER')")
    public User save(User user) {
        return postLoadProcessing(
            userRepository.save(
                prePersistProcessing(user)));
    }


    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(UUID uuid) {
        if (searchService.findByUserId( uuid ).size() > 0)
            throw new ReferenceInUseException( uuid.toString() );
        userRepository.deleteById(uuid);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(List<User> instances) {
        userRepository.deleteAll(instances);
    }


    private User prePersistProcessing(User instance) {
        if (instance.getAuthorities().size() == 0) {
            LOG.info( "prePersistProcessing -> ROLE_LIMITED" );
            instance.setAuthorities(authorityService.findAll()
                .stream().filter(i->i.getAuthority() == "ROLE_LIMITED")
                .collect(Collectors.toSet()));
        }
        if (instance.getId() == null) {
            LOG.info( "set default password" );
            instance.setPassword( "$2a$10$O1MMi3SLcvwtJIT9CSZyN.aLtFKN.K2LtKyHZ52wElo0zh5gI1EyW" );
        }
        return instance;
    }


    private User postLoadProcessing(User instance) {
        return instance;
    }


    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserNotFoundException(email)
        );
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UserNotFoundException(name)
        );
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<User> getByName(String name, Pageable pageable) {
        LOG.info( pageable.toString() );
        return  userRepository.findByUsernameIgnoreCaseLike( likeify(name) , pageable );
    }



    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasPermission('USER')")
    public String setPassword(Password instance) {
        LOG.info("old " + instance.getOldPassword());

        User user = userRepository.findById( instance.getId() )
            .orElseThrow(  () -> new UserNotFoundException(instance.getId()) );

        LOG.info("old on server " + user.getPassword());

        if (!passwordEncoder().matches( instance.getOldPassword(), user.getPassword() )) {
            throw new UserDeniedAuthorizationException( "Password mismatch ()" );
        }

        userRepository.setPassword( user.getId(), passwordEncoder().encode( instance.getPassword() ) );
        return "{ \"message\" : \"Password changed successfully\"}";
    }



}