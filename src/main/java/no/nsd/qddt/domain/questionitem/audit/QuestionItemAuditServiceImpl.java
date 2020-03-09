package no.nsd.qddt.domain.questionitem.audit;

import no.nsd.qddt.domain.AbstractAuditFilter;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
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
@Service("questionItemAuditService")
class QuestionItemAuditServiceImpl extends AbstractAuditFilter<Integer,QuestionItem> implements QuestionItemAuditService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final QuestionItemAuditRepository questionItemAuditRepository;

    private final ElementLoader rdLoader;

    private boolean showPrivateComments;

    @Autowired
    public QuestionItemAuditServiceImpl(QuestionItemAuditRepository questionItemAuditRepository, ResponseDomainAuditService responseDomainAuditService) {
        this.questionItemAuditRepository = questionItemAuditRepository;
        this.rdLoader =  new ElementLoader( responseDomainAuditService );
    }

    @Override
    public Revision<Integer, QuestionItem> findLastChange(UUID uuid) {
        return postLoadProcessing(questionItemAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, QuestionItem> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(questionItemAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisions(UUID uuid, Pageable pageable) {
        return questionItemAuditRepository.findRevisions(uuid, pageable).
            map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, QuestionItem> findFirstChange(UUID uuid) {
        return postLoadProcessing(
            questionItemAuditRepository.findRevisions(uuid)
                .reverse().getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments = showPrivate;
    }

    @Override
    public Page<Revision<Integer, QuestionItem>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        return getPage(questionItemAuditRepository.findRevisions(id),changeKinds,pageable);
    }

    @Override
    public Revision<Integer, QuestionItem> getQuestionItemLastOrRevision(UUID id, Integer revision) {
        Revision<Integer, QuestionItem> retval;
        if (revision == null || revision <= 0)
            retval = findLastChange(id);
        else
            retval = findRevision(id, revision);
        if (retval == null)
            LOG.info("getQuestionItemLastOrRevision returned with null (" + id + "," + revision + ")");
        return retval;
    }

    @Override
    protected Revision<Integer, QuestionItem> postLoadProcessing(Revision<Integer, QuestionItem> instance){
        if (instance.getEntity().getResponseDomainRef().getElementId() != null) {
            rdLoader.fill( instance.getEntity().getResponseDomainRef() );
        }
        Hibernate.initialize( instance.getEntity().getComments() );
        instance.getEntity().getVersion().setRevision( instance.getRevisionNumber() );

        return instance;
    }


}
