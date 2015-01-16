package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface CommentRepository  extends RevisionRepository<Comment, Long, Integer>, JpaRepository<Comment, Long> {
}
