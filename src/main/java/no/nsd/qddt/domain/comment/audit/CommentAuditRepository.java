package no.nsd.qddt.domain.comment.audit;

import no.nsd.qddt.domain.comment.Comment;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface CommentAuditRepository extends EnversRevisionRepository<Comment, UUID, Integer> {

}