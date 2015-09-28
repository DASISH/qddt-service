package no.nsd.qddt.repository;

import no.nsd.qddt.domain.User;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface UserRepository extends  EnversRevisionRepository<User, UUID, Integer> {

    /**
     * Return a user.
     *
     * @param email of the user we want
     * @return user with the given email
     */
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}
