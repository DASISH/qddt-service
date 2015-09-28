package no.nsd.qddt.service;

import no.nsd.qddt.domain.User;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import no.nsd.qddt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Service("userService")
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    @Transactional(readOnly = true)
    public List<User> findAll() { return userRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findAll(Iterable<UUID> uuids) {
        return userRepository.findAll(uuids);
    }

    @Override
    @Transactional(readOnly = false)
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID uuid) {
        userRepository.delete(uuid);
    }


    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email)
        );
    }
}