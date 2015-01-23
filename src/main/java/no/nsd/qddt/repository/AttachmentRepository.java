package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Stig Norland
 */
@Repository
public interface AttachmentRepository extends RevisionRepository<Attachment, Long, Integer>, JpaRepository<Attachment, Long> {}
