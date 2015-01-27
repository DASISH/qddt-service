package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AttachmentService {

    Attachment findById(UUID id);

    List<Attachment> findAll();

    Attachment save(Attachment attachment);

    public Revision<Integer, Attachment> findLastChange(UUID id);

    public Page<Revision<Integer, Attachment>> findAllRevisionsPageable(Attachment attachment, Pageable pageable);

}
