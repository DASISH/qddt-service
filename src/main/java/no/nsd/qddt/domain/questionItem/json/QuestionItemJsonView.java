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
        if (questionItem == null) return;
        setQuestion(questionItem.getQuestion());
        setResponseDomain(new ResponseDomainJsonView(questionItem.getResponseDomain()));
    }


    public Question getQuestion() {
        return question;
    }

    private void setQuestion(Question question) {
        if (question==null)
            System.out.println("Question is null!");
        else
            this.question = question;
    }

    public ResponseDomainJsonView getResponseDomain() {
        return responseDomain;
    }

    private void setResponseDomain(ResponseDomainJsonView responseDomain) {
        this.responseDomain = responseDomain;
    }
}
