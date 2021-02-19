package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.classes.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainAuditService extends BaseServiceAudit<ResponseDomain,UUID, Integer> {

    Page<Revision<Integer, ResponseDomain>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
