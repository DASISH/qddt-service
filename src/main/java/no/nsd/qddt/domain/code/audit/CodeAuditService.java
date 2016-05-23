package no.nsd.qddt.domain.code.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.code.Code;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeAuditService extends BaseServiceAudit<Code, UUID, Integer> {

}