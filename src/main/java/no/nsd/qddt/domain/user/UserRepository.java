package no.nsd.qddt.domain.user;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface UserRepository extends BaseRepository<User, UUID> {

    /**
     * Return a user.
     *
     * @param email of the user we want
     * @return user with the given email
     */
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String name);
}
