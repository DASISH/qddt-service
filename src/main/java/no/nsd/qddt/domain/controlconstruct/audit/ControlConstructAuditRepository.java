package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface ControlConstructAuditRepository extends RevisionRepository<ControlConstruct, UUID, Integer> {

    Page<Revision<Integer,ControlConstruct>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
