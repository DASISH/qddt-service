package no.nsd.qddt.utils.builders;


import no.nsd.qddt.domain.question.Question;

/**
 * @author Dag Østgulen Heradstveit
 */
public class QuestionBuilder {

    private String changeComment;
    private String name;

    public QuestionBuilder setChangeComment(String changeComment) {
        this.changeComment = changeComment;
        return this;
    }

    public QuestionBuilder setName(String name) {
        this.name = name;
        return this;
    }


    public Question createQuestion() {
        Question question = new Question();
        return question;
    }
}
