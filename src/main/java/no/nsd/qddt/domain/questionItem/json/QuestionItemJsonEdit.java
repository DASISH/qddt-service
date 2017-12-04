package no.nsd.qddt.domain.questionItem.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonEdit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonEdit extends BaseJsonEdit {

        private ResponseDomainJsonEdit responseDomain;

        private Integer responseDomainRevision;

        private String question;

        private String intent;

        private Set<CommentJsonEdit> comments = new HashSet<>();

        private List<ConceptRef> conceptRefs;


    public QuestionItemJsonEdit() {

    }


    public QuestionItemJsonEdit(QuestionItem questionItem) {
        super(questionItem);
        if (questionItem == null) return;
        setComments(questionItem.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
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

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public List<ConceptRef> getConceptRefs() {
        return conceptRefs;
    }

    private void setConceptRefs(List<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }
}


