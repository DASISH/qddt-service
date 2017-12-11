package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.exception.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("responseDomainAuditService")
class ResponseDomainAbstractAuditServiceImpl extends AbstractAuditFilter<Long,ResponseDomain> implements ResponseDomainAuditService {

    private final ResponseDomainAuditRepository responseDomainAuditRepository;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    public ResponseDomainAbstractAuditServiceImpl(ResponseDomainAuditRepository responseDomainAuditRepository, CommentService commentService) {
        this.responseDomainAuditRepository = responseDomainAuditRepository;
        this.commentService = commentService;
    }

    @Override
    public Revision<Long, ResponseDomain> findLastChange(UUID uuid) {
        return postLoadProcessing(responseDomainAuditRepository.findLastChangeRevision(uuid).get());
    }

    @Override
    public Revision<Long, ResponseDomain> findRevision(UUID uuid, Long revision) {
        return postLoadProcessing(responseDomainAuditRepository.findRevision(uuid, revision).get());
    }

    @Override
    public Page<Revision<Long, ResponseDomain>> findRevisions(UUID uuid, Pageable pageable) {
        return responseDomainAuditRepository.findRevisions(uuid,pageable)
                .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Long, ResponseDomain> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            responseDomainAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Long, ResponseDomain>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(responseDomainAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    protected Revision<Long, ResponseDomain> postLoadProcessing(Revision<Long, ResponseDomain> instance) {
        assert  (instance != null);
        try{
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getEntity().getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getEntity().getId());
            instance.getEntity().setComments(new HashSet<>(coms));
            instance.getEntity().getManagedRepresentation();        //Lazy loading trick... (we want the MR when locking at a revision).
        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        return instance;
    }

}
