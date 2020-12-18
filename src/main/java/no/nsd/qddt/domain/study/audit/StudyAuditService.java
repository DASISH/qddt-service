package no.nsd.qddt.domain.study.audit;

import no.nsd.qddt.classes.AbstractEntityAudit;
import no.nsd.qddt.classes.interfaces.BaseServiceAudit;
import no.nsd.qddt.domain.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface StudyAuditService extends BaseServiceAudit<Study, UUID, Integer> {

    Page<Revision<Integer, Study>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}
