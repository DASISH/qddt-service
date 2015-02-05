package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentService extends BaseServiceAudit<Comment> {

    //public Page<Comment> findSiblingsPageable(Long id, Pageable pageable);

    //Comment findById(UUID id);
}
