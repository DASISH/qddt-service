package no.nsd.qddt.domain.questionitem.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonView;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonView extends AbstractJsonEdit {

    private String question;

    private String intent;

    private ResponseDomainJsonView responseDomain;

    public QuestionItemJsonView(QuestionItem questionItem) {
        super(questionItem);
        if (questionItem == null) return;
        question = questionItem.getQuestion();
        intent = questionItem.getIntent();
        setResponseDomain(new ResponseDomainJsonView(questionItem.getResponseDomainRef().getElement()));
    }

    public String getQuestion() {
        return question;
    }

    public String getIntent() {
        return intent;
    }

    public ResponseDomainJsonView getResponseDomain() {
        return responseDomain;
    }

    private void setResponseDomain(ResponseDomainJsonView responseDomain) {
        this.responseDomain = responseDomain;
    }
}
