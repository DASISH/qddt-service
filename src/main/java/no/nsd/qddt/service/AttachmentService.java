package no.nsd.qddt.service;

import no.nsd.qddt.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AttachmentService extends BaseService<Attachment> {

    /**
     *
     * @param id
     * @return
     */
    Attachment findById(UUID id);

    /**
     *
     * @param moduleId
     * @param pageable
     * @return
     */
    Page<Attachment> findAllByModule(Long  moduleId, Pageable pageable);

    /**
     *
     * @param id
     * @param pageable
     * @return
     */
    Page<Attachment> findAllByGuid(UUID id, Pageable pageable);


}
