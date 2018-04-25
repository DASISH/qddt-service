package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationRepository extends BaseRepository<Publication,UUID> {

    @Query(value = "SELECT p.* FROM publication p " +
        "LEFT JOIN publication_status ps ON p.status_id = ps.id " +
        "WHERE (  ps.published in :published and (p.name ILIKE :name or p.purpose ILIKE :purpose) ) "
        + "ORDER BY ?#{#pageable}"
        ,countQuery = "SELECT count(p.*) FROM publication p " +
        "LEFT JOIN publication_status ps ON p.status_id = ps.id " +
        "WHERE (  ps.published in :published and (p.name ILIKE :name or p.purpose ILIKE :purpose) ) "
        ,nativeQuery = true)
    Page<Publication> findByQuery(PublicationStatus.Published[] published, String name, String purpose, Pageable pageable);


    Page<Publication> findByStatus_IdAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(Long statusIds, String name, String purpose,Pageable pageable);

    Page<Publication> findByNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(String name, String purpose,Pageable pageable);
}
