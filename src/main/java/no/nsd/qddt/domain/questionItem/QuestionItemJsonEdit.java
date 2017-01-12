package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.question.QuestionJson;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomainJsonEdit;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonEdit extends BaseJsonEdit {

    private ResponseDomainJsonEdit responseDomain;

    private Integer responseDomainRevision;

    private QuestionJson question;

    private Set<Comment> comments = new HashSet<>();

    private Set<ConceptRef> conceptRefs;

    public QuestionItemJsonEdit() {

    }

    public QuestionItemJsonEdit(QuestionItem questionItem) {
        super(questionItem);
        if (questionItem == null) return;
        setComments(questionItem.getComments());
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<ConceptRef> getConceptRefs() {
        return conceptRefs;
    }

    public void setConceptRefs(Set<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }
}


