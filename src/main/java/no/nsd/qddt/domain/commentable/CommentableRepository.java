package no.nsd.qddt.domain.commentable;

import no.nsd.qddt.domain.comment.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit.
 */
@Repository
interface CommentableRepository extends CrudRepository<Comment, UUID> {

    List<Comment> findByOwnerId(UUID ownerId);
}
