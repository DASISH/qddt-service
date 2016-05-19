package no.nsd.qddt.utils.builders;

import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;

/**
 * @author Dag Østgulen Heradstveit
 */
public class QuestionItemBuilder {

    private String name;
    private Question question;
    private ResponseDomain responseDomain;

    public QuestionItemBuilder setQuestion(Question question) {
        this.question = question;
        return this;
    }

    public QuestionItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public QuestionItemBuilder setResponseDomain(ResponseDomain responseDomain){
        this.responseDomain = responseDomain;
        return this;
    }


    public QuestionItem createQuestionItem() {
        QuestionItem questionItem = new QuestionItem();
        questionItem.setQuestion(question);
        questionItem.setResponseDomain(responseDomain);
        question.setName(this.name);
        return questionItem;
    }
}
