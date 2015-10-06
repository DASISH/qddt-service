package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.study.Study;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface StudyAuditService extends BaseServiceAudit<Study, UUID, Integer> {

}
