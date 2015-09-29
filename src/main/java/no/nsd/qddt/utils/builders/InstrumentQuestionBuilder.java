package no.nsd.qddt.utils.builders;

import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;

/**
 * @author Dag Østgulen Heradstveit
 */
public class InstrumentQuestionBuilder {

    private String changeComment;
    private String name;
    private Instrument instrument;
    private Question question;


    public InstrumentQuestionBuilder setChangeComment(String changeComment) {
        this.changeComment = changeComment;
        return this;
    }

    public InstrumentQuestionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public InstrumentQuestionBuilder setInstrument(Instrument instrument) {
        this.instrument = instrument;
        return this;
    }

    public InstrumentQuestionBuilder setQuestion(Question question) {
        this.question = question;
        return this;
    }

    public InstrumentQuestion createInstrument() {
        InstrumentQuestion instrumentQuestion = new InstrumentQuestion();
        instrumentQuestion.setName(this.name);
        instrumentQuestion.setChangeComment(this.changeComment);
        instrumentQuestion.setInstrument(this.instrument);
        instrumentQuestion.setQuestion(this.question);

        return instrumentQuestion;
    }
}
