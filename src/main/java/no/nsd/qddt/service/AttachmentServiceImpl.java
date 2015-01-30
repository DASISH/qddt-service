package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import no.nsd.qddt.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public Attachment findById(UUID id) {return attachmentRepository.findOne(id);}

//    @Override
//    @Transactional(readOnly = true)
//    public Page<Attachment> findSiblingsPageable(Attachment instance, Pageable pageable) {
//        return attachmentRepository.findSiblingsPageable(instance,pageable);
//    }

    @Override
    @Transactional(readOnly = true)
    public Attachment findById(Long id) {return null;}

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public Page<Attachment> findAll(Pageable pageable) {return attachmentRepository.findAll(pageable);  }

    @Override
    @Transactional(readOnly = false)
    public Attachment save(Attachment instance) {

        instance.setCreated(LocalDateTime.now());
        return attachmentRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Attachment instance) { attachmentRepository.delete(instance);
    }

}
