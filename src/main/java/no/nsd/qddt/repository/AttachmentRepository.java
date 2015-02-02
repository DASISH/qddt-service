package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AttachmentRepository extends EnversRevisionRepository<Attachment, UUID, Integer> {
    //Page<Attachment> findSiblingsPageable(Attachment instance, Pageable pageable);

}
