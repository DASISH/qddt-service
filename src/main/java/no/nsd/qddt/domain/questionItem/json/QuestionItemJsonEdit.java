package no.nsd.qddt.domain.questionitem.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonEdit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonEdit extends AbstractJsonEdit {

        private ResponseDomainJsonEdit responseDomain;

        private Integer responseDomainRevision;

        private String question;

        private String intent;

        private List<CommentJsonEdit> comments = new ArrayList<>();

        private List<ConceptRef> conceptRefs;


    public QuestionItemJsonEdit() {

    }


    public QuestionItemJsonEdit(QuestionItem questionItem) {
        super(questionItem);
        if (questionItem == null) return;
        setComments(questionItem.getComments());
        setQuestion(questionItem.getQuestion());
        setIntent(questionItem.getIntent());
        if (questionItem.getResponseDomain() != null)
            setResponseDomain(new ResponseDomainJsonEdit(questionItem.getResponseDomain()));
        setResponseDomainRevision(questionItem.getResponseDomainRevision());
        setConceptRefs(questionItem.getConceptRefs());

    }

    public ResponseDomainJsonEdit getResponseDomain() {
        return responseDomain;
    }

    private void setResponseDomain(ResponseDomainJsonEdit responseDomain) {
        this.responseDomain = responseDomain;
    }

    public Integer getResponseDomainRevision() {
        return responseDomainRevision;
    }

    private void setResponseDomainRevision(Integer responseDomainRevision) {
        this.responseDomainRevision = responseDomainRevision;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(List<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public List<ConceptRef> getConceptRefs() {
        return conceptRefs;
    }

    private void setConceptRefs(List<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }
}


