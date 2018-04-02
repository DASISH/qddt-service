package no.nsd.qddt.domain.controlconstruct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
@Entity
@Audited
@DiscriminatorValue("QUESTION_CONSTRUCT")
public class QuestionConstruct  extends ControlConstruct {
    //------------- Begin QuestionItem revision early bind "hack" ---------------

    /**
     * This field will be populated with the correct version of a QI,
     * but should never be persisted.
     */
    @JsonSerialize
    @JsonDeserialize
    @Transient
    private QuestionItem questionItem;

    /**
     * This field must be available "raw" in order to set and query
     * questionItem by ID
     */
    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="questionitem_id")
    private UUID questionItemUUID;


    @Column(name = "questionitem_revision")
    private Integer questionItemRevision;

    private String questionName;

    private String questionText;

    //------------- End QuestionItem revision early bind "hack"------------------

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER )
    @OrderColumn(name="universe_idx")
    private List<Universe> universe =new ArrayList<>(0);


    @JsonIgnore
    @OrderColumn(name="instruction_idx")
    @OrderBy("instruction_idx ASC")
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

    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem question) {
        this.questionItem = question;
    }

    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    public UUID getQuestionItemUUID() {
        return questionItemUUID;
    }

    private void setQuestionItemUUID(UUID questionItem) {
        questionItemUUID = questionItem;
    }

    /**
     * @return the questionName
     */
    public String getQuestionName() {
        return questionName;
    }

    /**
     * @param questionName the questionName to set
     */
    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    /**
     * @return the questionText
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * @param questionText the questionText to set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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
        harvestInstructions(ControlConstructInstructionRank.POST, getPostInstructions());
        if (this.getQuestionItem() != null)
            setQuestionItemUUID(this.getQuestionItem().getId());
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
        pdfReport.addParagraph(getQuestionItem().getQuestion());

        getQuestionItem().getResponseDomain().fillDoc(pdfReport,"");

        if (getPostInstructions().size() > 0)
            pdfReport.addheader2("Post Instructions");
        for(Instruction post:getPostInstructions()){
            pdfReport.addParagraph(post.getDescription());
        }

        if(getComments().size()>0)
            pdfReport.addheader2("Comments");
        pdfReport.addComments(getComments());

        pdfReport.addPadding();
    }

    @Override
    protected void beforeUpdate() {}
    @Override
    protected void beforeInsert() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionConstruct)) return false;
        if (!super.equals( o )) return false;

        QuestionConstruct that = (QuestionConstruct) o;

        if (questionItemUUID != null ? !questionItemUUID.equals( that.questionItemUUID ) : that.questionItemUUID != null)
            return false;
        if (questionItemRevision != null ? !questionItemRevision.equals( that.questionItemRevision ) : that.questionItemRevision != null)
            return false;
        if (universe != null ? !universe.equals( that.universe ) : that.universe != null) return false;
        return controlConstructInstructions != null ? controlConstructInstructions.equals( that.controlConstructInstructions ) : that.controlConstructInstructions == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (questionItemUUID != null ? questionItemUUID.hashCode() : 0);
        result = 31 * result + (questionItemRevision != null ? questionItemRevision.hashCode() : 0);
        result = 31 * result + (universe != null ? universe.hashCode() : 0);
        result = 31 * result + (controlConstructInstructions != null ? controlConstructInstructions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"QuestionConstruct\":"
            + super.toString()
            + ", \"questionItemUUID\":" + questionItemUUID
            + ", \"questionItemRevision\":\"" + questionItemRevision + "\""
            + ", \"universe\":" + universe
            + ", \"controlConstructInstructions\":" + controlConstructInstructions
            + "}";
    }


}
