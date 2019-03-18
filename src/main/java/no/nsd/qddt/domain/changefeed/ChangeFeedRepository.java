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


    @Query(value = "SELECT cl.* FROM change_log cl " +
        "WHERE (  cl.name ILIKE :name or cl.ref_change_kind ILIKE :changeKind or cl.ref_kind ILIKE :kind) "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(cl) FROM change_log cl " +
        "WHERE (  cl.name ILIKE :name or cl.ref_change_kind ILIKE :changeKind or cl.ref_kind ILIKE :kind) "
        ,nativeQuery = true)
    Page<ChangeFeed> findByQuery(
        @Param("name")String name,
        @Param("changeKind")String changeKind,
        @Param("kind")String kind, Pageable pageable);

}
