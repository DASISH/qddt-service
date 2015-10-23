package no.nsd.qddt.domain.group.audit;

import no.nsd.qddt.domain.group.Group;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface GroupAuditRepository  extends EnversRevisionRepository<Group, UUID, Integer> {
}
