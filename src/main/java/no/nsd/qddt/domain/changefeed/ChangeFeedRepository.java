package no.nsd.qddt.domain.changefeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface ChangeFeedRepository extends JpaRepository<ChangeFeed,ChangeFeedKey> {

    Page<ChangeFeed> findByNameLikeIgnoreCaseOrRefChangeKindLikeIgnoreCaseOrRefKindLikeIgnoreCase
        (String name, String changeKind, String kind, Pageable pageable);


    @Query(value = "SELECT cl FROM ChangeFeed cl " +
        "WHERE  lower(cl.name) like :name or lower(cl.refChangeKind) LIKE :changeKind or lower(cl.refKind) LIKE :kind "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(cl) FROM ChangeFeed cl " +
        "WHERE  lower(cl.name) like :name or lower(cl.refChangeKind) LIKE :changeKind or lower(cl.refKind) LIKE :kind "
        + "ORDER BY ?#{#pageable}"
        ,nativeQuery = false)
    Page<ChangeFeed> findByQuery(
        @Param("name")String name,
        @Param("changeKind")String changeKind,
        @Param("kind")String kind, Pageable pageable);

}
