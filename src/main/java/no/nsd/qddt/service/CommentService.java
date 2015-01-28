package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentService extends AbstractService<Comment> {

    public Page<Comment> findSiblingsPageable(Long id, Pageable pageable);

}
