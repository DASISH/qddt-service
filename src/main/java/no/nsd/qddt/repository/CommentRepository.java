package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Comment;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface CommentRepository extends BaseRepository<Comment>, EnversRevisionRepository<Comment, Long, Integer> {

//    Page<Comment> findCommentByParentOrderByIdAsc(Comment parent, Pageable pageable);
}
