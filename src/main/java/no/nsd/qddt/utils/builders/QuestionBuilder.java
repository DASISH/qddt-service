package no.nsd.qddt.utils.builders;

import no.nsd.qddt.domain.question.Question;

/**
 * @author Dag Østgulen Heradstveit
 */
public class QuestionBuilder {

    private String changeComment;
    private String name;
    private String instructions;

    public QuestionBuilder setChangeComment(String changeComment) {
        this.changeComment = changeComment;
        return this;
    }

    public QuestionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public QuestionBuilder setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public Question createInstrument() {
        Question question = new Question();
        question.setName(this.name);
        question.setChangeComment(this.changeComment);

        return question;
    }
}
