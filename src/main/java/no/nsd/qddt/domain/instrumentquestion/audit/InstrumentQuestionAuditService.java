package no.nsd.qddt.domain.instrumentquestion.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentQuestionAuditService extends BaseServiceAudit<InstrumentQuestion,UUID, Integer> {

}
