package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
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
    private final CommentService commentService;
    private boolean showPrivateComments;


    @Autowired
    public ControlConstructAuditServiceImpl(ControlConstructAuditRepository ccAuditRepository
            , QuestionItemAuditService qAuditService
            , CommentService cService) {
        this.controlConstructAuditRepository = ccAuditRepository;
        this.qiAuditService = qAuditService;
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
    private QuestionConstruct postLoadProcessing(QuestionConstruct instance) {
        assert  (instance != null);
        try{
            // FIX BUG instructions doesn't load within ControlConstructAuditServiceImpl, by forcing read here, it works...
            // https://github.com/DASISH/qddt-client/issues/350
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

        } catch (Exception ex){
            LOG.error("removeQuestionItem",ex);
        }
        return instance;
    }


    private ControlConstruct postLoadProcessing(ControlConstruct instance) {
        assert  (instance != null);

//        List<Comment> coms  =commentService.findAllByOwnerId(instance.getId(),showPrivateComments);

//        instance.setComments(new ArrayList<>(coms));

        return  instance.getClassKind().equals("QUESTION_CONSTRUCT")?postLoadProcessing( (QuestionConstruct) instance ): instance;
    }


    @Override
    protected Revision<Integer, ControlConstruct> postLoadProcessing(Revision<Integer, ControlConstruct> instance) {
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber() );

        return new Revision<>(instance.getMetadata(), postLoadProcessing(instance.getEntity()));
    }
}
