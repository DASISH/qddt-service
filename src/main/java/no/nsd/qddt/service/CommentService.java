package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentService {

    public Comment findById(Long id);

    public Comment save(Comment comment);

    public void delete(Comment comment);

    public Revision<Integer, Comment> findLastChange(Long id);

    public Page<Revision<Integer, Comment>> findAllRevisionsPageable(Comment comment , int min, int max);
}
