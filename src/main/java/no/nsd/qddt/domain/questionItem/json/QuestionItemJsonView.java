package no.nsd.qddt.domain.questionItem.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonView;

/**
 * @author Stig Norland
 */
public class QuestionItemJsonView extends BaseJsonEdit{

    private Question question;

    private ResponseDomainJsonView responseDomain;

    public QuestionItemJsonView(QuestionItem questionItem) {
        super(questionItem);
        setQuestion(questionItem.getQuestion());
        setResponseDomain(new ResponseDomainJsonView(questionItem.getResponseDomain()));
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ResponseDomainJsonView getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomainJsonView responseDomain) {
        this.responseDomain = responseDomain;
    }
}
