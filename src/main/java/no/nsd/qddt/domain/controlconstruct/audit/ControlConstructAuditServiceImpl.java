package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditQuestionService")
class ControlConstructAuditServiceImpl extends AbstractAuditFilter<Integer,ControlConstruct> implements ControlConstructAuditService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ControlConstructAuditRepository controlConstructAuditRepository;
    private final QuestionItemAuditService qiAuditService;
    private final OtherMaterialService otherMaterialService;
    private final CommentService commentService;
    private boolean showPrivateComments;


    @Autowired
    public ControlConstructAuditServiceImpl(ControlConstructAuditRepository ccAuditRepository
            , QuestionItemAuditService qAuditService
            , CommentService cService
            , OtherMaterialService oService) {
        this.controlConstructAuditRepository = ccAuditRepository;
        this.qiAuditService = qAuditService;
        this.otherMaterialService = oService;
        this.commentService = cService;
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findLastChange(UUID id) {
        return postLoadProcessing(controlConstructAuditRepository.findLastChangeRevision(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, ControlConstruct> findRevision(UUID id, Integer revision) {
        return postLoadProcessing(controlConstructAuditRepository.findRevision(id, revision));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, ControlConstruct>> findRevisions(UUID id, Pageable pageable) {
        return controlConstructAuditRepository.findRevisions(id,pageable)
            .map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, ControlConstruct> findFirstChange(UUID uuid) {
        PageRequest pageable = new PageRequest(0,1);
        return  postLoadProcessing(
                controlConstructAuditRepository.findRevisions(uuid,
                defaultSort(pageable,"RevisionNumber DESC")).getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments = showPrivate;
    }

    @Override
    public Page<Revision<Integer, ControlConstruct>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(controlConstructAuditRepository.findRevisions(id),changeKinds,pageable);
    }


    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
     */
    private  ControlConstruct postLoadProcessing(ControlConstruct instance){
        assert  (instance != null);
        try{
            // FIX BUG instructions doesn't load within ControlConstructAuditServiceImpl, by forcing read here, it works...
            // https://github.com/DASISH/qddt-client/issues/350
//            System.out.println("postLoadProcessing instruction -> " + instance.getControlConstructInstructions().size());
            instance.populateInstructions();

            if(instance.getQuestionItemUUID() != null) {
                if (instance.getQuestionItemRevision() == null || instance.getQuestionItemRevision() <= 0) {
                    Revision<Integer, QuestionItem> rev = qiAuditService.findLastChange(instance.getQuestionItemUUID());
                    instance.setQuestionItemRevision(rev.getRevisionNumber());
                    instance.setQuestionItem(rev.getEntity());
                } else {
                    QuestionItem qi  =qiAuditService.findRevision(instance.getQuestionItemUUID(), instance.getQuestionItemRevision()).getEntity();
                    instance.setQuestionItem(qi);
                }
            }
            else
                instance.setQuestionItemRevision(0);

            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());

            instance.setComments(new HashSet<>(coms));

        } catch (Exception ex){
            LOG.error("removeQuestionItem",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        }

        return instance;
    }


    @Override
    protected Revision<Integer, ControlConstruct> postLoadProcessing(Revision<Integer, ControlConstruct> instance) {
        return new Revision<>(instance.getMetadata(), postLoadProcessing(instance.getEntity()));
    }
}
