package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.questionitem.QuestionItem;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConstructQuestionJsonView  extends ConstructJsonView {

    private UUID questionItemUUID;
    
    private Integer questionItemRevision;

    private String questionName;

    private String questionText;

    private String universe;

    public ConstructQuestionJsonView(QuestionConstruct construct) {
        super(construct);
        if (construct.getQuestionItemRef() != null) {
            questionItemUUID = construct.getQuestionItemRef().getElementId();
            questionName = construct.getQuestionItemRef().getName();
            questionText = construct.getQuestionItemRef().getText();
            questionItemRevision = construct.getQuestionItemRef().getElementRevision();
        } else {
            questionName = "?";
        }
        universe =  construct.getUniverse().stream().map( s -> s.getDescription() ).collect( Collectors.joining("/ ") );
    }

    /**
     * @return the questionItemUUID
     */
    public UUID getQuestionItemUUID() {
        return questionItemUUID;
    }

    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    /**
     * @return the questionName
     */
    public String getQuestionName() {
        return questionName;
    }

    /**
     * @return the questionText
     */
    public String getQuestionText() {
        return questionText;
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
