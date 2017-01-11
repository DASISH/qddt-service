package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.question.QuestionJson;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.ResponseDomainJsonView;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonEdit extends BaseJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private ResponseDomainJsonView responseDomain;

    private Integer responseDomainRevision;

    private QuestionJson question;

    private Set<Comment> comments = new HashSet<>();

    private Set<ConceptRef> conceptRefs;

    @Type(type="pg-uuid")
    private UUID basedOnObject;

    public QuestionItemJsonEdit() {

    }

    public QuestionItemJsonEdit(QuestionItem questionItem) {
        super(questionItem);
        if (questionItem == null) return;
        setId(questionItem.getId());
        setName(questionItem.getName());
        setComments(questionItem.getComments());
        setQuestion(new QuestionJson(questionItem.getQuestion()));
        if (questionItem.getResponseDomain() != null)
            setResponseDomain(new ResponseDomainJsonView(questionItem.getResponseDomain()));
        setResponseDomainRevision(questionItem.getResponseDomainRevision());
        setBasedOnObject(questionItem.getBasedOnObject());
        setConceptRefs(questionItem.getConceptRefs());

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseDomainJsonView getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomainJsonView responseDomain) {
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

    public UUID getBasedOnObject() {
        return basedOnObject;
    }

    public void setBasedOnObject(UUID basedOnObject) {
        this.basedOnObject = basedOnObject;
    }

    public Set<ConceptRef> getConceptRefs() {
        return conceptRefs;
    }

    public void setConceptRefs(Set<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }
}


