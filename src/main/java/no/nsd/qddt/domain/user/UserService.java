package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface UserService  extends BaseService<User, UUID> {

    User findByEmail(String email);

    Page<User> findByName(String name, Pageable pageable);

    void setPassword(Password instance);

}

