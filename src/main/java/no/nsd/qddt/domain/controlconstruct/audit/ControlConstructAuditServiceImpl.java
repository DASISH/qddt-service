package no.nsd.qddt.domain.controlconstruct.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import org.hibernate.Hibernate;
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
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentAuditQuestionService")
class ControlConstructAuditServiceImpl extends AbstractAuditFilter<Integer,ControlConstruct> implements ControlConstructAuditService {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ControlConstructAuditRepository controlConstructAuditRepository;
    private final ElementLoader<QuestionItem> qidLoader;
    private boolean showPrivateComments;


    @Autowired
    public ControlConstructAuditServiceImpl(ControlConstructAuditRepository ccAuditRepository
            , QuestionItemAuditService qAuditService) {
        this.controlConstructAuditRepository = ccAuditRepository;
        this.qidLoader = new ElementLoader<>( qAuditService);
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
//            Hibernate.initialize(instance.getControlConstructInstructions());
            instance.populateInstructions();

            if (instance.getControlConstructInstructions().size() > 0 ) {
                LOG.info("here they are: " + instance.getControlConstructInstructions().stream()
                    .map( p -> p.getInstruction().getName() )
                    .collect( Collectors.joining(", ")));
            }
            if(instance.getQuestionItemRef().getElementId() != null) {
                qidLoader.fill( instance.getQuestionItemRef() );
            }

        } catch (Exception ex){
            LOG.error("removeQuestionItem",ex);
        }
        return instance;
    }


    private ControlConstruct postLoadProcessing(ControlConstruct instance) {
        assert  (instance != null);
//            Hibernate.initialize(instance.getComments());

        return  instance.getClassKind().equals("QUESTION_CONSTRUCT")?postLoadProcessing( (QuestionConstruct) instance ): instance;
    }


    @Override
    protected Revision<Integer, ControlConstruct> postLoadProcessing(Revision<Integer, ControlConstruct> instance) {
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber() );

        return new Revision<>(instance.getMetadata(), postLoadProcessing(instance.getEntity()));
    }
}
