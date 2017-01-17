package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.question.QuestionJson;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomainJsonEdit;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonEdit extends BaseJsonEdit {

    private ResponseDomainJsonEdit responseDomain;

    private Integer responseDomainRevision;

    private QuestionJson question;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private Set<ConceptRef> conceptRefs;

    public QuestionItemJsonEdit() {

    }

    public QuestionItemJsonEdit(QuestionItem questionItem) {
        super(questionItem);
        if (questionItem == null) return;
        setComments(questionItem.getComments().stream().map(F-> new CommentJsonEdit(F)).collect(Collectors.toSet()));
        setQuestion(new QuestionJson(questionItem.getQuestion()));
        if (questionItem.getResponseDomain() != null)
            setResponseDomain(new ResponseDomainJsonEdit(questionItem.getResponseDomain()));
        setResponseDomainRevision(questionItem.getResponseDomainRevision());
        setConceptRefs(questionItem.getConceptRefs());

    }

    public ResponseDomainJsonEdit getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomainJsonEdit responseDomain) {
        this.responseDomain = responseDomain;
    }

    public Integer getResponseDomainRevision() {
        return responseDomainRevision;
    }

    public void setResponseDomainRevision(Integer responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }

    public QuestionJson getQuestion() {
        return question;
    }

    public void setQuestion(QuestionJson question) {
        this.question = question;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    public void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public Set<ConceptRef> getConceptRefs() {
        return conceptRefs;
    }

    public void setConceptRefs(Set<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }
}


