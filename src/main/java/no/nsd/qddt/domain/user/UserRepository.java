package no.nsd.qddt.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);

    Page<User> findByUsernameIgnoreCaseLike(String name, Pageable pageable);

    @Modifying
    @Query(value = "update user_account set password = :passwordEncrypted where id = :uuid" ,nativeQuery = true )
    void setPassword(@Param("uuid") UUID uuid, @Param("passwordEncrypted") String passwordEncrypted);
}

