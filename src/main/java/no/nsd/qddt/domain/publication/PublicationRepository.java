package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationRepository extends BaseRepository<Publication,UUID> {

    Page<Publication> findByStatusPublishedInAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(PublicationStatus.Published[] published, String name, String purpose, Pageable pageable);

    Page<Publication> findByStatus_IdAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(Long statusIds, String name, String purpose,Pageable pageable);
}
