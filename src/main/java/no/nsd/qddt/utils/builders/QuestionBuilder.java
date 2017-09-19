package no.nsd.qddt.utils.builders;


import no.nsd.qddt.domain.question.Question;

/**
 * @author Dag Østgulen Heradstveit
 */
public class QuestionBuilder {

    public QuestionBuilder setChangeComment(String changeComment) {
        return this;
    }

    public QuestionBuilder setName(String name) {
        return this;
    }


    public Question createQuestion() {
        return new Question();
    }
}
