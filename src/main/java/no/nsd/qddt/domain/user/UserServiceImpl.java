package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.role.AuthorityService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Service("userService")
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityService authorityService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
    }


    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return userRepository.exists(uuid);
    }

    @Override
    public User findOne(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, User.class)
        );
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public User save(User user) {
        return postLoadProcessing(
                userRepository.save(
                        prePersistProcessing(user)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<User> save(List<User> instances) {
        return userRepository.save(instances);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(UUID uuid) {
        userRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(List<User> instances) {
        userRepository.delete(instances);
    }


    private User prePersistProcessing(User instance) {
        if (instance.getAuthorities().size() == 0) {
            instance.setAuthorities(authorityService.findAll()
                .stream().filter(i->i.getAuthority() == "ROLE_LIMITED")
                .collect(Collectors.toSet()));
        }
        return instance;
    }


    private User postLoadProcessing(User instance) {
        return instance;
    }


    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
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

        return  userRepository.findByUsernameIgnoreCaseLike( likeify(name) , pageable );
    }


}