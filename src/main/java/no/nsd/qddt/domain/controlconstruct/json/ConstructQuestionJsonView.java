package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.questionItem.QuestionItem;

import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConstructQuestionJsonView  extends ConstructJsonView {

    private QuestionItemSimpleJson questionItem;

    private Integer questionItemRevision;

    private String universe;

    public ConstructQuestionJsonView(QuestionConstruct construct) {
        super(construct);
        questionItem = new QuestionItemSimpleJson(construct.getQuestionItem());
        questionItemRevision = construct.getQuestionItemRevision();
        universe =  construct.getUniverse().stream().map( s -> s.getDescription() ).collect( Collectors.joining("/ ") );
    }

    public QuestionItemSimpleJson getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItemSimpleJson questionItem) {
        this.questionItem = questionItem;
    }

    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }


    public String getUniverse() {
        return universe;
    }

    public class QuestionItemSimpleJson {
        String name;

        String question;

        public QuestionItemSimpleJson(QuestionItem questionItem) {
            if (questionItem == null)
                return;
            name = questionItem.getName();
            question = questionItem.getQuestion();
        }


        public String getName() {
            return name;
        }

        public String getQuestion() {
            return question;
        }

    }
}
