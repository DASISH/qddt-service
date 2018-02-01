package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.controlconstructparameter.ResponseReference;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.othermaterial.OtherMaterialCC;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Stig Norland
 */
public class ConstructQuestionJson  extends ConstructJson {

    private QuestionItemSimpleJson questionItem;

    private Long questionItemRevision;

    private String label;

    private String description;

    private Set<OtherMaterialCC> otherMaterials;

    private List<ResponseReference> parameters = new ArrayList<>();

    private List<Instruction> preInstructions;

    private List<Instruction> postInstructions;

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

    public Long getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Long questionItemRevision) {
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

    public Set<OtherMaterialCC> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterialCC> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public List<ResponseReference> getParameters() {
        return parameters;
    }

    public void setParameters(List<ResponseReference> parameters) {
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
