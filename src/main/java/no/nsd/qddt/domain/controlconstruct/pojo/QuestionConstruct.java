package no.nsd.qddt.domain.controlconstruct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.classes.elementref.ElementKind;
import no.nsd.qddt.domain.classes.elementref.ElementRefQuestionItem;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.domain.classes.xml.AbstractXmlBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("QUESTION_CONSTRUCT")
public class QuestionConstruct  extends ControlConstruct {

    @Column(name = "description",length = 1500)
    private String description;

    @Embedded
    private ElementRefQuestionItem elementRefQuestionItem;

    //------------- Begin QuestionItem revision early bind "hack" ---------------
    //------------- End QuestionItem revision early bind "hack"------------------

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn(name="universe_idx")
    private List<Universe> universe =new ArrayList<>(0);


    @OrderColumn(name="instruction_idx")
    @ElementCollection(fetch = FetchType.EAGER )
    @CollectionTable(name = "CONTROL_CONSTRUCT_INSTRUCTION",
        joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")})
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>();

    public QuestionConstruct() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ElementRefQuestionItem getQuestionItemRef() {
        return elementRefQuestionItem;
    }

    public void setQuestionItemRef(ElementRefQuestionItem elementRefQuestionItem) {
        this.elementRefQuestionItem = elementRefQuestionItem;
    }

    public List<Universe> getUniverse() {
        return universe;
    }

    public void setUniverse(List<Universe> universe) {
        this.universe = universe;
    }

    public List<ControlConstructInstruction> getControlConstructInstructions() {
        return controlConstructInstructions;
    }

    public void setControlConstructInstructions(List<ControlConstructInstruction> controlConstructInstructions) {
        this.controlConstructInstructions = controlConstructInstructions;
    }

    @JsonIgnore
    public List<Instruction> getPreInstructions() {
        return getControlConstructInstructions().stream()
            .filter(i->i.getInstructionRank().equals(ControlConstructInstructionRank.PRE))
            .map(ControlConstructInstruction::getInstruction)
            .collect( Collectors.toList());
    }

    @JsonIgnore
    public List<Instruction> getPostInstructions() {
        return getControlConstructInstructions().stream()
            .filter(i->i.getInstructionRank().equals(ControlConstructInstructionRank.POST))
            .map(ControlConstructInstruction::getInstruction)
            .collect( Collectors.toList());
    }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals( o )) return false;

        QuestionConstruct that = (QuestionConstruct) o;

        if (description != null ? !description.equals( that.description ) : that.description != null) return false;
        return elementRefQuestionItem != null ? elementRefQuestionItem.equals( that.elementRefQuestionItem ) : that.elementRefQuestionItem == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (elementRefQuestionItem != null ? elementRefQuestionItem.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{ " +
            "\"id\":" + (getId() == null ? "null" : getId()) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"description\":" + (description == null ? "null" : "\"" + description + "\"") + ", " +
            "\"questionItemRef\":" + (elementRefQuestionItem == null ? "null" : elementRefQuestionItem) + ", " +
            "\"modified\":" + (getModified() == null ? "null" : getModified()) +
            "}";
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new ControlConstructFragmentBuilder<QuestionConstruct>( this ) {
            @Override
            public void addXmlFragments(Map<ElementKind, Map<String, String>> fragments) {
                super.addXmlFragments( fragments );
                if (children.size() == 0) addChildren();
                children.stream().forEach( c -> c.addXmlFragments( fragments ) );
            }
            @Override
            public String getXmlFragment() {
                if (children.size() == 0) addChildren();
                return super.getXmlFragment();
            }

            private void addChildren() {
                children.add( getQuestionItemRef().getElement().getXmlBuilder()  );
                children.addAll( getUniverse().stream().map( u -> u.getXmlBuilder() ).collect( Collectors.toList()) );
                children.addAll( getControlConstructInstructions().stream().map( u -> u.getInstruction().getXmlBuilder() ).collect( Collectors.toList()) );
            }
        };
    }

    @Override
    public void fillDoc(PdfReport pdfReport, String counter)  {
        pdfReport.addHeader(this, "ControlConstruct " + counter);

        pdfReport.addParagraph( this.getDescription() );

        if (getUniverse().size() > 0)
            pdfReport.addheader2("Universe");
        for(Universe uni:getUniverse()){
            pdfReport.addParagraph(uni.getDescription());
        }

        if (getPreInstructions().size() > 0)
            pdfReport.addheader2("Pre Instructions");
        for(Instruction pre:getPreInstructions()){
            pdfReport.addParagraph(pre.getDescription());
        }

        pdfReport.addheader2("Question Item");
        pdfReport.addParagraph(getQuestionItemRef().getElement().getQuestion());

        getQuestionItemRef().getElement().getResponseDomainRef().getElement().fillDoc(pdfReport,"");

        if (getPostInstructions().size() > 0)
            pdfReport.addheader2("Post Instructions");
        for(Instruction post:getPostInstructions()){
            pdfReport.addParagraph(post.getDescription());
        }

        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());

        // pdfReport.addPadding();
    }

}
