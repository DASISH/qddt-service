package no.nsd.qddt.domain.publication.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.publication.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("publicationAuditService")
class PublicationAbstractAuditServiceImpl extends AbstractAuditFilter<Long,Publication> implements PublicationAuditService {

    private final PublicationAuditRepository publicationAuditRepository;

    @Autowired
    public PublicationAbstractAuditServiceImpl(PublicationAuditRepository instrumentRepository) {
        this.publicationAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Long, Publication> findLastChange(UUID uuid) {
        return publicationAuditRepository.findLastChangeRevision(uuid).get();
    }

    @Override
    public Revision<Long, Publication> findRevision(UUID uuid, Long revision) {
        return publicationAuditRepository.findRevision(uuid, revision).get();
    }

    @Override
    public Page<Revision<Long, Publication>> findRevisions(UUID uuid, Pageable pageable) {
        return publicationAuditRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Revision<Long, Publication> findFirstChange(UUID uuid) {
        return publicationAuditRepository.findRevisions(uuid).reverse().getContent().get(0);
    }


    @Override
    public Page<Revision<Long, Publication>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(publicationAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    // we don't have an interface for editing instructions, hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

    @Override
    protected Revision<Long, Publication> postLoadProcessing(Revision<Long, Publication> instance) {
        return instance;
    }
    
}

