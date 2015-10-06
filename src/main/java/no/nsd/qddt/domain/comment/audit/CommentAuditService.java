package no.nsd.qddt.domain.comment.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.comment.Comment;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CommentAuditService extends BaseServiceAudit<Comment, UUID,Integer> {


}
