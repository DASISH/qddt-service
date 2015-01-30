package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AttachmentService extends AbstractService<Attachment>{

    public Attachment findById(UUID id);

//    public Page<Attachment> findSiblingsPageable(Attachment instance, Pageable pageable);



}
