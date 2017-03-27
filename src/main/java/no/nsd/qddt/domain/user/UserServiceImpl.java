package no.nsd.qddt.domain.user;

import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Service("userService")
class UserServiceImpl implements UserService{

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
    @Transactional(readOnly = false)
    public User save(User user) {
        return postLoadProcessing(
                userRepository.save(
                        prePersistProcessing(user)));
    }

    @Override
    public List<User> save(List<User> instances) {
        return userRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        userRepository.delete(uuid);
    }

    @Override
    public void delete(List<User> instances) {
        userRepository.delete(instances);
    }


    protected User prePersistProcessing(User instance) {
        return instance;
    }


    protected User postLoadProcessing(User instance) {
        return instance;
    }


    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email)
        );
    }
}