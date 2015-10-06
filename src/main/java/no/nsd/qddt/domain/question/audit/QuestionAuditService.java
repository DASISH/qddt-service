package no.nsd.qddt.domain.question.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.question.Question;

import java.util.UUID;


/**
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionAuditService extends BaseServiceAudit<Question, UUID, Integer> {

}
