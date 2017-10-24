package no.nsd.qddt.domain.concept.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentService;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.conceptquestionitem.ParentQuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.exception.StackTraceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("conceptAuditService")
class ConceptAuditServiceImpl implements ConceptAuditService {

    private final ConceptAuditRepository conceptAuditRepository;
    private final QuestionItemAuditService questionAuditService;
    private final CommentService commentService;
    private boolean showPrivateComments;

    @Autowired
    ConceptAuditServiceImpl(ConceptAuditRepository conceptAuditRepository,
                            QuestionItemAuditService questionAuditService,
                            CommentService commentService
    ){
        this.conceptAuditRepository = conceptAuditRepository;
        this.questionAuditService = questionAuditService;
        this.commentService = commentService;
    }

    private static int compare(Revision<Integer, Concept> f, Revision<Integer, Concept> g) {
        return f.getRevisionNumber();
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
                map(this::postLoadProcessing);
    }

    @Override
    public Revision<Integer, Concept> findFirstChange(UUID uuid) {
        return postLoadProcessing(conceptAuditRepository.findRevisions(uuid).
            getContent().get(0));
    }

    @Override
    public void setShowPrivateComment(boolean showPrivate) {
        showPrivateComments=showPrivate;
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
            conceptAuditRepository.findRevisions(id).reverse().getContent().stream()
                .filter(f->!changeKinds.contains(f.getEntity().getChangeKind()))
                .skip(skip)
                .limit(limit)
                .map(this::postLoadProcessing)
                .collect(Collectors.toList()));
    }

    @Override
    public Page<Revision<Integer, Concept>> findRevisionsByChangeKindIncludeLatest(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable) {
        int skip = pageable.getOffset();
        int limit = pageable.getPageSize();
        return new PageImpl<>(
                Stream.concat(
                        Stream.of(conceptAuditRepository.findRevisions(id).getLatestRevision())
                                .map(e->{
                                    e.getEntity().getVersion().setVersionLabel("Latest version");
                                    return e;
                                }),
                        conceptAuditRepository.findRevisions(id).reverse().getContent().stream()
                                .filter(f->!changeKinds.contains(f.getEntity().getChangeKind()))
                )
                        .skip(skip)
                        .limit(limit)
                        .map(this::postLoadProcessing)
                        .collect(Collectors.toList()));
    }

    private Revision<Integer, Concept> postLoadProcessing(Revision<Integer, Concept> instance) {
        assert  (instance != null);
        return new Revision<>(
                instance.getMetadata(),
                postLoadProcessing(instance.getEntity()));
    }

    private Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);

        try{
            List<Comment> coms;
            if (showPrivateComments)
                coms = commentService.findAllByOwnerId(instance.getId());
            else
                coms  =commentService.findAllByOwnerIdPublic(instance.getId());
            instance.setComments(new HashSet<>(coms));
            instance.getConceptQuestionItems()
                    .forEach(cqi-> cqi.setQuestionItem(
                            getQuestionItemLastOrRevision(cqi)));

            instance.getChildren().stream().map(this::postLoadProcessing);

        } catch (Exception ex){
            StackTraceFilter.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        }
        return instance;
    }

    private QuestionItem getQuestionItemLastOrRevision(ParentQuestionItem cqi){
        return questionAuditService.getQuestionItemLastOrRevision(
                cqi.getId().getQuestionItemId(),
                cqi.getQuestionItemRevision()).getEntity();
    }
}
