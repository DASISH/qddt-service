package no.nsd.qddt.domain.commentable;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.comment.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit.
 */
@Repository
interface CommentableRepository extends BaseRepository<Comment, UUID> {

    List<Comment> findByOwnerId(UUID ownerId);
}
