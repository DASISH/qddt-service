package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
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
    public Revision<Integer, ResponseDomain> findLastChange(UUID id) {
        return postLoadProcessing(responseDomainAuditRepository.findLastChangeRevision(id).get());
    }

    @Override
    public Revision<Integer, ResponseDomain> findRevision(UUID id, Integer revision) {
        return postLoadProcessing(responseDomainAuditRepository.findRevision(id, revision).get());
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisions(UUID id, Pageable pageable) {
        return responseDomainAuditRepository.findRevisions(id,pageable)
                .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, ResponseDomain> findFirstChange(UUID id) {
        return postLoadProcessing(
            responseDomainAuditRepository.findRevisions(id)
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
