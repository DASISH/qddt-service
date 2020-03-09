package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.QuestionConstruct;
import no.nsd.qddt.domain.instruction.json.InstructionJsonView;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonView;
import no.nsd.qddt.domain.universe.Universe;

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
        if (construct.getQuestionItemRef() != null) {
            if (construct.getQuestionItemRef().getElement() != null)
                questionItem = new QuestionItemSimpleJson( construct.getQuestionItemRef().getElement() );
            questionItemRevision = construct.getQuestionItemRef().getElementRevision();
        }
        preInstructions = construct.getPreInstructions().stream().map( InstructionJsonView::new ).collect(Collectors.toList());
        postInstructions = construct.getPostInstructions().stream().map( InstructionJsonView::new ).collect(Collectors.toList());
        universe =  construct.getUniverse().stream().map( Universe::getDescription ).collect( Collectors.joining("/ ") );
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
            // responseDomain =  new ResponseDomainJsonView(questionitem.getResponseDomain());
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
