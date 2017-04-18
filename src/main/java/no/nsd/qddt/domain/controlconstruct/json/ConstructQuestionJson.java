package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.parameter.CCParameter;
import no.nsd.qddt.domain.questionItem.QuestionItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Stig Norland
 */
public class ConstructQuestionJson  extends ConstructJson {

    private QuestionItemSimpleJson questionItem;

    private Integer questionItemRevision;

    private String label;

    private String description;

    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    private List<CCParameter> parameters = new ArrayList<>();

    private List<Instruction> preInstructions =new ArrayList<>();

    private List<Instruction> postInstructions =new ArrayList<>();

    public ConstructQuestionJson(ControlConstruct construct) {
        super(construct);
        questionItem = new QuestionItemSimpleJson(construct.getQuestionItem());
        questionItemRevision = construct.getQuestionItemRevision();
        label = construct.getLabel();
        description = construct.getDescription();
        otherMaterials = construct.getOtherMaterials();
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public List<CCParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<CCParameter> parameters) {
        this.parameters = parameters;
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

        Question question;

        public QuestionItemSimpleJson(QuestionItem questionItem) {
            if (questionItem == null)
                return;
            name = questionItem.getName();
            question = new Question(questionItem.getQuestion().getQuestion());
        }

        public class Question {
            String question;

            public Question(String question) {
                this.question = question;
            }

            public String getQuestion() {
                return question;
            }
        }

        public String getName() {
            return name;
        }

        public Question getQuestion() {
            return question;
        }
    }
}
