package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;

import java.util.List;

/**
 * @author Stig Norland
 */
public class ConstructQuestionJson  extends ConstructJson {

    private QuestionItemSimpleJson questionItem;

    private Integer questionItemRevision;

    private List<Instruction> preInstructions;

    private List<Instruction> postInstructions;

    public ConstructQuestionJson(QuestionConstruct construct) {
        super(construct);
        questionItem = new QuestionItemSimpleJson(construct.getQuestionItem());
        questionItemRevision = construct.getQuestionItemRevision();
        preInstructions = construct.getPreInstructions();
        postInstructions = construct.getPostInstructions();
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


    public List<Instruction> getPreInstructions() {
        return preInstructions;
    }

    public void setPreInstructions(List<Instruction> preInstructions) {
        this.preInstructions = preInstructions;
    }

    public List<Instruction> getPostInstructions() {
        return postInstructions;
    }

    public void setPostInstructions(List<Instruction> postInstructions) {
        this.postInstructions = postInstructions;
    }

    public class QuestionItemSimpleJson {
        String name;

        String question;

        ResponseDomain responseDomain;

        public QuestionItemSimpleJson(QuestionItem questionItem) {
            if (questionItem == null)
                return;
            name = questionItem.getName();
            question = questionItem.getQuestion();
            responseDomain = questionItem.getResponseDomain();
        }


        public String getName() {
            return name;
        }

        public String getQuestion() {
            return question;
        }

        public ResponseDomain getResponseDomain() {
            return responseDomain;
        }
    }
}
