package no.nsd.qddt.security.user;

import no.nsd.qddt.classes.interfaces.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String name);

    Page<User> findByUsernameIgnoreCaseLike(String name, Pageable pageable);

    @Modifying
//    @Query("update User u set u.password = :passwordEncrypted  where u.id = :id")
    @Query(value = "update user_account set password = :passwordEncrypted where id = :uuid" ,nativeQuery = true )
    void setPassword(@Param("uuid") UUID uuid, @Param("passwordEncrypted") String passwordEncrypted);

}
