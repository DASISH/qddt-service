package no.nsd.qddt.domain.publication;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationRepository extends BaseRepository<Publication,UUID>{

    Page<Publication> findByStatusLikeAndNameIgnoreCaseLikeOrPurposeIgnoreCaseLike(String status, String name, String purpose,Pageable pageable);
}
