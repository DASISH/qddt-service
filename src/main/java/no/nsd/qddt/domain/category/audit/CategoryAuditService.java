package no.nsd.qddt.domain.category.audit;

import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.classes.interfaces.BaseServiceAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryAuditService extends BaseServiceAudit<Category, UUID, Integer > {

    Page<Revision<Integer, Category>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

//    Revision<Integer,Category> findVersion(UUID id, String version);
}
