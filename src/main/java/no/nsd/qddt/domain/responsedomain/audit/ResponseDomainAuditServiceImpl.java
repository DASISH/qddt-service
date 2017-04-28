package no.nsd.qddt.domain.responsedomain.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("responseDomainAuditService")
class ResponseDomainAuditServiceImpl implements ResponseDomainAuditService {

    private ResponseDomainAuditRepository responseDomainAuditRepository;
    private CommentService commentService;

    @Autowired
    public ResponseDomainAuditServiceImpl(ResponseDomainAuditRepository responseDomainAuditRepository,CommentService commentService) {
        this.responseDomainAuditRepository = responseDomainAuditRepository;
        this.commentService = commentService;
    }

    @Override
    public Revision<Integer, ResponseDomain> findLastChange(UUID uuid) {
        return postLoadProcessing(responseDomainAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, ResponseDomain> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(responseDomainAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisions(UUID uuid, Pageable pageable) {
        return responseDomainAuditRepository.findRevisions(uuid,pageable)
                .map(c-> postLoadProcessing(c));
    }

    @Override
    public Revision<Integer, ResponseDomain> findFirstChange(UUID uuid) {
        return responseDomainAuditRepository.findRevisions(uuid).
                getContent().stream()
                .min((i,o)->i.getRevisionNumber())
                .map(c-> postLoadProcessing(c)).get();
    }

    @Override
    public Page<Revision<Integer, ResponseDomain>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                responseDomainAuditRepository.findRevisions(id).getContent().stream()
                        .filter(f -> !changeKinds.contains(f.getEntity().getChangeKind()))
                        .skip(skip)
                        .limit(limit)
                        .map(c-> postLoadProcessing(c))
                        .collect(Collectors.toList())
        );
    }

    protected Revision<Integer, ResponseDomain> postLoadProcessing(Revision<Integer, ResponseDomain> instance) {
        assert  (instance != null);
        postLoadProcessing(instance.getEntity());
        return instance;
    }

    protected ResponseDomain postLoadProcessing(ResponseDomain instance) {
        assert  (instance != null);

        try{
            List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
            instance.setComments(new HashSet<>(coms));
            instance.getManagedRepresentation();        //Lazy loading trick... (we want the MR when locking at a revision).
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;
    }
}
