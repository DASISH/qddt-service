package no.nsd.qddt.utils.builders;

import no.nsd.qddt.domain.questionitem2.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;

/**
 * @author Dag Østgulen Heradstveit
 */
public class QuestionItemBuilder {

    private String question;
    private ResponseDomain responseDomain;

    public QuestionItemBuilder setQuestion(String question) {
        this.question = question;
        return this;
    }

    public QuestionItemBuilder setName(String name) {
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
        return questionItem;
    }
}
