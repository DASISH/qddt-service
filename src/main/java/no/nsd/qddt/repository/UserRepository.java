package no.nsd.qddt.repository;

import no.nsd.qddt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Return a user.
     *
     * @param email of the user we want
     * @return user with the given email
     */
    Optional<User> findByEmail(String email);
}
