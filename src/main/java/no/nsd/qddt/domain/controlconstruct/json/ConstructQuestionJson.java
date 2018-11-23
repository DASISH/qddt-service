package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.instruction.json.InstructionJsonView;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConstructQuestionJson  extends ConstructJson {

    private static final long serialVersionUID = 1L;

	private QuestionItemSimpleJson questionItem;

    private Integer questionItemRevision;

    private List<InstructionJsonView> preInstructions;

    private List<InstructionJsonView> postInstructions;

    private String universe;

    public ConstructQuestionJson(QuestionConstruct construct) {
        super(construct);
        questionItem = new QuestionItemSimpleJson(construct.getQuestionItem());
        questionItemRevision = construct.getQuestionItemRevision();
        preInstructions = construct.getPreInstructions().stream().map( map -> new InstructionJsonView(map)).collect(Collectors.toList());
        postInstructions = construct.getPostInstructions().stream().map( map -> new InstructionJsonView(map)).collect(Collectors.toList());
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

    public List<InstructionJsonView> getPreInstructions() {
        return preInstructions;
    }

    public void setPreInstructions(List<InstructionJsonView> preInstructions) {
        this.preInstructions = preInstructions;
    }

    public List<InstructionJsonView> getPostInstructions() {
        return postInstructions;
    }

    public void setPostInstructions(List<InstructionJsonView> postInstructions) {
        this.postInstructions = postInstructions;
    }

    public String getUniverse() {
        return universe;
    }

    public class QuestionItemSimpleJson {
        String name;

        String question;

        ResponseDomainJsonView responseDomain;

        public QuestionItemSimpleJson(QuestionItem questionItem) {
            if (questionItem == null)
                return;
            name = questionItem.getName();
            question = questionItem.getQuestion();
            // responseDomain =  new ResponseDomainJsonView(questionItem.getResponseDomain());
        }


        public String getName() {
            return name;
        }

        public String getQuestion() {
            return question;
        }

        public ResponseDomainJsonView getResponseDomain() {
            return responseDomain;
        }
    }
}
