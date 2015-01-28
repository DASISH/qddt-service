package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentServiceImpl(AttachmentRepository attachmentRepository){
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Attachment findById(UUID id) {
        return attachmentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findAll() {
        return attachmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Attachment save(Attachment attachment) {   return attachmentRepository.save(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Attachment> findLastChange(UUID id) {
        return attachmentRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Attachment>> findAllRevisionsPageable(Attachment attachment, Pageable pageable) {
        return attachmentRepository.findRevisions(attachment.getGuid(),pageable);
    }
}
