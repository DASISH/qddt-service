package no.nsd.qddt.domain.controlconstruct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.embedded.QuestionItemRef;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private QuestionItemRef questionItemRef;

    @Transient
    @JsonSerialize
    private Set<String> parameter = new HashSet<>(0);

    //------------- Begin QuestionItem revision early bind "hack" ---------------
    //------------- End QuestionItem revision early bind "hack"------------------

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn(name="universe_idx")
    private List<Universe> universe =new ArrayList<>(0);


    @JsonIgnore
    @OrderColumn(name="instruction_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_INSTRUCTION",
        joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")})
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>(0);


    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany
    private List<Instruction> preInstructions =new ArrayList<>();


    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany
    private List<Instruction> postInstructions =new ArrayList<>();

    public QuestionConstruct() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionItemRef getQuestionItemRef() {
        return questionItemRef;
    }

    public void setQuestionItemRef(QuestionItemRef questionItemRef) {
        this.questionItemRef = questionItemRef;
    }

    public Set<String> getParameter() {
        return parameter;
    }

    public void setParameter(Set<String> parameter) {
        this.parameter = parameter;
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

    /*
    fetches pre and post instructions and add them to ControlConstructInstruction
     */
    public void populateControlConstructInstructions() {
        if (controlConstructInstructions == null)
            controlConstructInstructions = new ArrayList<>();
        else
            controlConstructInstructions.clear();

        harvestInstructions( ControlConstructInstructionRank.PRE, getPreInstructions());
        harvestInstructions( ControlConstructInstructionRank.POST, getPostInstructions());
    }

    private void harvestInstructions(ControlConstructInstructionRank rank,List<Instruction> instructions) {
        try {
            for (Instruction instruction : instructions) {
                ControlConstructInstruction cci = new ControlConstructInstruction();
                cci.setInstruction(instruction);
                cci.setInstructionRank(rank);
                this.getControlConstructInstructions().add(cci);
            }
        }catch (Exception ex){
            LOG.error("harvestPostInstructions exception",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
    }

    /*
     this function is useful for populating ControlConstructInstructions after loading from DB
      */
    public void populateInstructions(){
        setPreInstructions(getControlConstructInstructions().stream()
            .filter(i->i.getInstructionRank().equals(ControlConstructInstructionRank.PRE))
            .map(ControlConstructInstruction::getInstruction)
            .collect( Collectors.toList()));

        setPostInstructions(getControlConstructInstructions().stream()
            .filter(i->i.getInstructionRank().equals(ControlConstructInstructionRank.POST))
            .map(ControlConstructInstruction::getInstruction)
            .collect(Collectors.toList()));
    }

    public List<Instruction> getPreInstructions() {
        return preInstructions;
    }

    private void setPreInstructions(List<Instruction> preInstructions) {
        this.preInstructions = preInstructions;
    }

    public List<Instruction> getPostInstructions() {
        return postInstructions;
    }

    private void setPostInstructions(List<Instruction> postInstructions) {
        this.postInstructions = postInstructions;
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
        return questionItemRef != null ? questionItemRef.equals( that.questionItemRef ) : that.questionItemRef == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (questionItemRef != null ? questionItemRef.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{ " +
            "\"id\":" + (getId() == null ? "null" : getId()) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"description\":" + (description == null ? "null" : "\"" + description + "\"") + ", " +
            "\"questionItemRef\":" + (questionItemRef == null ? "null" : questionItemRef) + ", " +
            "\"modified\":" + (getModified() == null ? "null" : getModified()) +
            "}";
    }


}
