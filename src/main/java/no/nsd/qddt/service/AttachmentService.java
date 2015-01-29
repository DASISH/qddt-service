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
public interface AttachmentService extends AbstractService<Attachment>{

    public Attachment findById(UUID id);

//    public Page<Attachment> findSiblingsPageable(Attachment instance, Pageable pageable);



}
