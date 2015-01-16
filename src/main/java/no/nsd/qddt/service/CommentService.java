package no.nsd.qddt.service;

import no.nsd.qddt.domain.Comment;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentService {

    public Comment findById(Long id);

    public Comment save(Comment comment);

    public void delete(Comment comment);
}
