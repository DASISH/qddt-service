package no.nsd.qddt.domain.comment;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CommentRepository extends BaseRepository<Comment,UUID> {

    /**
     * @param ownerId Is a parentUUID.
     * @param userId User Id of caller
     * @return All attachments that belongs to the module with moduleId.
     */

    @Query(value =
        "SELECT co.* FROM comment co " +
        "WHERE co.owner_id = :ownerId " +
        "AND ( (  :showAll  and (SELECT authority FROM uar WHERE id = :userId ) IN ('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT') )" +
        "OR co.is_public = true )" +
        "ORDER BY co.updated asc "
        ,countQuery = "SELECT  count(co.*) FROM comment co " +
        "WHERE co.owner_id = :ownerId " +
        "AND ( (  :showAll  and (SELECT authority FROM uar WHERE id = :userId ) IN ('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT') )" +
        "OR co.is_public = true )"
        ,nativeQuery = true)

    List<Comment> findByQuery(@Param("ownerId")UUID ownerId, @Param("userId")UUID userId, @Param("showAll")boolean showAll);


    /**
     * @param ownerId Is a parentUUID.
     * @param userId User Id of caller
     * @param pageable Pageable object
     * @return All attachments that belongs to the module with moduleId.
     */

    @Query(value =
        "SELECT co.* FROM comment co " +
            "WHERE co.owner_id = :ownerId " +
            "AND ( (  :showAll  and (SELECT authority FROM uar WHERE id = :userId ) IN ('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT') )" +
            "OR co.is_public = true )" +
            "ORDER BY ?#{#pageable} "
        ,countQuery = "SELECT  count(co.*) FROM comment co " +
        "WHERE co.owner_id = :ownerId " +
        "AND ( (  :showAll  and (SELECT authority FROM uar WHERE id = :userId ) IN ('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT') )" +
        "OR co.is_public = true )"
        ,nativeQuery = true)
    Page<Comment> findByQueryPageable(@Param("ownerId")UUID ownerId, @Param("userId")UUID userId, @Param("showAll")boolean showAll, Pageable pageable);

}
