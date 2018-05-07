package no.nsd.qddt.domain.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface SearchRepository extends JpaRepository<QddtUrl,UUID> {

    List<QddtUrl> findByName(String name);

    List<QddtUrl> findByUserId(UUID userId);

//    @Query(value = "SELECT p.*  FROM publication p " +
//        "LEFT JOIN publication_status ps ON p.status_id = ps.id " +
//        "WHERE (  ps.published in :published and (p.name ILIKE :name or p.purpose ILIKE :purpose) ) "
//        + "ORDER BY ?#{#pageable}"
//        ,countQuery = "SELECT count(p.*) FROM publication p " +
//        "LEFT JOIN publication_status ps ON p.status_id = ps.id " +
//        "WHERE (  ps.published in :published and (p.name ILIKE :name or p.purpose ILIKE :purpose) ) "
//        ,nativeQuery = true)
//    QddtUrl findByQuery(@Param("uuid")String uuid);
}
