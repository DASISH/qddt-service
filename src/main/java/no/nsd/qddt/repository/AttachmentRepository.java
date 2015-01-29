package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface AttachmentRepository extends RevisionRepository<Attachment, UUID, Integer>, JpaRepository<Attachment, UUID> {

    //Page<Attachment> findSiblingsPageable(Attachment instance, Pageable pageable);

}
