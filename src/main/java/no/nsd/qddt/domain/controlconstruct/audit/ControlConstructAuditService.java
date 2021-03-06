package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.classes.interfaces.BaseServiceAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ControlConstructAuditService extends BaseServiceAudit<ControlConstruct,UUID, Integer> {

    Page<Revision<Integer, ControlConstruct>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
