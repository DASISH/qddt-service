package no.nsd.qddt.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

//import static no.nsd.qddt.security.WebSecurityConfig.passwordEncoderBean;

/**
 * @author Stig Norland
 */
@Service
public class UserServiceImpl implements UserService {


    final private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(UUID id) {
        return repository.existsById( id );
    }

    @Override
    public User findOne(UUID id) {
        return  repository.getOne( id );
    }

//    @Override
//    public <S extends User> S findOne(UUID id) {
//        return (S) repository.getOne( id );
//    }

    @Override
    public <S extends User> S save(S instance) {
        return repository.save( instance );
    }

    @Override
    public void delete(UUID id) throws DataAccessException {
        repository.deleteById( id );
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmailIgnoreCase( email ).orElseThrow();
    }

    @Override
    public Page<User> findByName(String name, Pageable pageable) {
        return repository.findUsersByUsernameIsLikeOrEmailIsLike( name , name,pageable);
    }

    @Override
    public void setPassword(Password instance) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = repository.getOne( instance.id );

        if (!passwordEncoder.matches( instance.getOldPassword(), user.getPassword() )) {
            throw new BadCredentialsException("User or password incorrect");
        }

        String encryptedPassword =  passwordEncoder.encode( instance.getPassword() );
        repository.setPassword(instance.id, encryptedPassword  );
    }
}
