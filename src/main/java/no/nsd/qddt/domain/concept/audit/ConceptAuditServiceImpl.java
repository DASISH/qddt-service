package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("conceptAuditService")
class ConceptAuditServiceImpl implements ConceptAuditService {

    private ConceptAuditRepository conceptAuditRepository;

    @Autowired
    ConceptAuditServiceImpl(ConceptAuditRepository conceptAuditRepository){
        this.conceptAuditRepository = conceptAuditRepository;
    }

    @Override
    public Revision<Integer, Concept> findLastChange(UUID uuid) {
        return conceptAuditRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revision<Integer, Concept> findRevision(UUID uuid, Integer revision) {
        return conceptAuditRepository.findRevision(uuid, revision);
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisions(UUID uuid, Pageable pageable) {
        return conceptAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
          conceptAuditRepository.findRevisions(id).getContent().stream()
                  .filter(f->!changeKinds.contains(f.getEntity().getChangeKind()))
                  .skip(skip)
                  .limit(limit)
                  .collect(Collectors.toList())
        );
    }

}
