package no.nsd.qddt.domain.group.audit;

import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.group.Group;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface GroupAuditService extends BaseServiceAudit<Group, UUID,Integer> {
}
