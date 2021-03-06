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
 * @author Stig Norland
 **/

@Service("publicationAuditService")
class PublicationAuditServiceImpl extends AbstractAuditFilter<Integer,Publication> implements PublicationAuditService {

    private final PublicationAuditRepository publicationAuditRepository;

    @Autowired
    public PublicationAuditServiceImpl(PublicationAuditRepository instrumentRepository) {
        this.publicationAuditRepository = instrumentRepository;
    }

    @Override
    public Revision<Integer, Publication> findLastChange(UUID id) {
        return publicationAuditRepository.findLastChangeRevision(id).get();
    }

    @Override
    public Revision<Integer, Publication> findRevision(UUID id, Integer revision) {
        return publicationAuditRepository.findRevision(id, revision).get();
    }

    @Override
    public Page<Revision<Integer, Publication>> findRevisions(UUID id, Pageable pageable) {
        return publicationAuditRepository.findRevisions(id, pageable);
    }

    @Override
    public Revision<Integer, Publication> findFirstChange(UUID id) {
        return publicationAuditRepository.findRevisions(id).reverse().getContent().get(0);
    }


    @Override
    public Page<Revision<Integer, Publication>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(publicationAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    // we don't have an interface for editing instructions, hence we don't need to fetch comments that never are there...
    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        // no implementation
    }

    @Override
    protected Revision<Integer, Publication> postLoadProcessing(Revision<Integer, Publication> instance) {
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber().get() );

        return instance;
    }
    
}

