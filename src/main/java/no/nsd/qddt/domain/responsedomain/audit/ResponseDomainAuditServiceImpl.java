package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.classes.AbstractAuditFilter;
import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service("responseDomainAuditService")
class ResponseDomainAuditServiceImpl extends AbstractAuditFilter<Integer,ResponseDomain> implements ResponseDomainAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ResponseDomainAuditRepository responseDomainAuditRepository;
    private boolean showPrivateComments;

    @Autowired
    public ResponseDomainAuditServiceImpl(ResponseDomainAuditRepository responseDomainAuditRepository) {
        this.responseDomainAuditRepository = responseDomainAuditRepository;
    }

    @Override
    public Revision<Integer, ResponseDomain> findLastChange(UUID uuid) {
        return postLoadProcessing(responseDomainAuditRepository.findLastChangeRevision(uuid).get());
    }

    @Override
    public Revision<Integer, ResponseDomain> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(responseDomainAuditRepository.findRevision(uuid, revision).get());
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisions(UUID uuid, Pageable pageable) {
        return responseDomainAuditRepository.findRevisions(uuid,pageable)
                .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, ResponseDomain> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            responseDomainAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(responseDomainAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Integer, ResponseDomain> postLoadProcessing(Revision<Integer, ResponseDomain> instance) {
        assert  (instance != null);
        try{
            Hibernate.initialize( instance.getEntity().getComments() );
            Hibernate.initialize(instance.getEntity().getManagedRepresentation());  //Lazy loading trick... (we want the MRep when locking at a revision).
        } catch (Exception ex) {
            LOG.error("postLoadProcessing", ex);
        }
        return instance;
    }

}
