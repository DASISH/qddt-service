package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
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
@Service("conceptAuditService")
class ConceptAuditServiceImpl implements ConceptAuditService {

    private ConceptAuditRepository conceptAuditRepository;
    private QuestionItemAuditService questionAuditService;
    private CommentService commentService;

    @Autowired
    ConceptAuditServiceImpl(ConceptAuditRepository conceptAuditRepository,
                            QuestionItemAuditService questionAuditService,
                            CommentService commentService
    ){
        this.conceptAuditRepository = conceptAuditRepository;
        this.questionAuditService = questionAuditService;
        this.commentService = commentService;
    }

    @Override
    public Revision<Integer, Concept> findLastChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findLastChangeRevision(uuid));
    }

    @Override
    public Revision<Integer, Concept> findRevision(UUID uuid, Integer revision) {
        return postLoadProcessing(conceptAuditRepository.findRevision(uuid, revision));
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisions(UUID uuid, Pageable pageable) {
        return conceptAuditRepository.findRevisions(uuid, pageable).
                map(c-> postLoadProcessing(c));
    }

    @Override
    public Revision<Integer, Concept> findFirstChange(UUID uuid) {
        return conceptAuditRepository.findRevisions(uuid).
                getContent().stream().
                min((i,o)->i.getRevisionNumber()).
                map(c-> postLoadProcessing(c)).
                get();
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
          conceptAuditRepository.findRevisions(id).getContent().stream()
                  .filter(f->!changeKinds.contains(f.getEntity().getChangeKind()))
                  .skip(skip)
                  .limit(limit)
                  .map(c-> postLoadProcessing(c))
                  .collect(Collectors.toList())
        );
    }


    protected Revision<Integer, Concept> postLoadProcessing(Revision<Integer, Concept> instance) {
        assert  (instance != null);
        return new Revision<>(
                instance.getMetadata(),
                postLoadProcessing(instance.getEntity()));
    }

    protected Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);

        try{
            System.out.println("postLoadProcessing ConceptAuditService " + instance.getName()+ " QI:" + instance.getConceptQuestionItems().size());
            List<Comment> coms = commentService.findAllByOwnerId(instance.getId());
            instance.setComments(new HashSet<>(coms));
            instance.getConceptQuestionItems().forEach(cqi->{
                    System.out.println("    Fetching QI for cqi :");
                    cqi.setQuestionItem(questionAuditService.getQuestionItemLastOrRevision(
                            cqi.getId().getQuestionItemId(),
                            cqi.getQuestionItemRevision()).
                            getEntity());
                    System.out.println("    QI fetched: " + cqi.getQuestionItem().getName());
            });

            instance.getChildren().stream().map(c->postLoadProcessing(c));

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;
    }
}
