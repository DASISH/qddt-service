package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;

import java.util.UUID;


/**
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionItemAuditService extends BaseServiceAudit<QuestionItem, UUID, Integer> {

}
