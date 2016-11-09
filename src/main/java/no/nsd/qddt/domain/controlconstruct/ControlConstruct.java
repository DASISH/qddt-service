package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstructinstruction.ControlConstructInstruction;
import no.nsd.qddt.domain.controlconstructinstruction.InstructionRank;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.parameter.CCParameter;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instrument is the significant relation.
 * Instrument will be asked for all {@link Question} instances it has and the
 * metadata in this class will be used as visual logic for each {@link Question}.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "CONTROL_CONSTRUCT")
public class ControlConstruct extends AbstractEntityAudit {

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH}, orphanRemoval = true)
    @OrderColumn(name = "children_index")
    @JoinColumn(name = "parent_id")
    private Set<ControlConstruct> children = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @OrderColumn(name = "instrument_index")
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @Column(length = 300)
    private String indexRationale;

    private String label;

    @Column(length = 3000)
    private String description;

    /*
    This field should never be saved to db, QuestionItem needs to be handled manually. in the servicelayer.
     */
//    @ManyToOne()
//    @JoinColumn(name = "questionitem_id",updatable = false)
    @JsonSerialize
    @JsonDeserialize
    @Transient
    private QuestionItem questionItem;

    @Column(name="questionitem_UUID")
    @Type(type="pg-uuid")
    private UUID questionItemUUID;

    @Column(name = "questionitem_revision",nullable = false)
    private Integer revisionNumber;


    @OneToMany(fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    //TODO ArrayList dosn't work with Enver
    @OneToMany(mappedBy = "controlConstruct",fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.DETACH})
    @OrderColumn(name="instructions_idx")
    @JsonIgnore
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>();

    private String logic;

    //TODO ArrayList dosn't work with Enver
    @OneToMany
    private List<CCParameter> parameters = new ArrayList<>();


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


    public ControlConstruct() {
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem question) {
        this.questionItem = question;
        setQuestionItemUUID(question.getId());
    }

    public UUID getQuestionItemUUID() {
        return questionItemUUID;
    }

    public void setQuestionItemUUID(UUID questionItem) {
        questionItemUUID = questionItem;
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }


    public void addOtherMaterials(OtherMaterial otherMaterial) {
        getOtherMaterials().add(otherMaterial);
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

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public Set<ControlConstruct> getChildren() {
        return children;
    }

    public void setChildren(Set<ControlConstruct> children) {
        this.children = children;
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
    public void populateControlConstructs() {
        System.out.println("populateControlConstructs");
        if (controlConstructInstructions == null)
            controlConstructInstructions = new ArrayList<>();
        else
            controlConstructInstructions.clear();

        harvestPreInstructions(getPreInstructions());
        harvestPostInstructions(getPostInstructions());
        if (this.getQuestionItem() != null)
            setQuestionItemUUID(this.getQuestionItem().getId());
    }

    private void harvestPostInstructions(List<Instruction> instructions) {
        try {
            for (Instruction instruction : instructions) {
                ControlConstructInstruction cci = new ControlConstructInstruction();
                cci.setInstruction(instruction);
                cci.setInstructionRank(InstructionRank.POST);
                cci.setControlConstruct(this);
                this.getControlConstructInstructions().add(cci);
            }
        }catch (Exception ex){
            System.out.println("harvestPostInstructions exception " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void harvestPreInstructions(List<Instruction> instructions){
        try {
            for (int i = 0; i < instructions.size(); i++) {
                ControlConstructInstruction cci = new ControlConstructInstruction();
                cci.setInstruction(preInstructions.get(i));
                cci.setInstructionRank(InstructionRank.PRE);
                cci.setControlConstruct(this);
                this.controlConstructInstructions.add(i, cci);
            }
        }catch (Exception ex) {
            System.out.println("harvestPreInstructions exception " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /*
     this function is useful for populating ControlConstructInstructions after loading from DB
      */
    public void populateInstructions(){
        System.out.println("populateInstructions");
        setPreInstructions(getControlConstructInstructions().stream()
                .filter(i->i.getInstructionRank().equals(InstructionRank.PRE))
                .map(ControlConstructInstruction::getInstruction)
                .collect(Collectors.toList()));

        setPostInstructions(getControlConstructInstructions().stream()
                .filter(i->i.getInstructionRank().equals(InstructionRank.POST))
                .map(ControlConstructInstruction::getInstruction)
                .collect(Collectors.toList()));
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

    public String getIndexRationale() {
        return indexRationale;
    }

    public void setIndexRationale(String indexRationale) {
        this.indexRationale = indexRationale;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public List<CCParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<CCParameter> parameters) {
        this.parameters = parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstruct)) return false;
        if (!super.equals(o)) return false;

        ControlConstruct that = (ControlConstruct) o;

        if (getInstrument() != null ? !getInstrument().equals(that.getInstrument()) : that.getInstrument() != null)
            return false;
        if (getQuestionItem() != null ? !getQuestionItem().equals(that.getQuestionItem()) : that.getQuestionItem() != null)
            return false;
        if (getIndexRationale() != null ? !getIndexRationale().equals(that.getIndexRationale()) : that.getIndexRationale() != null)
            return false;
        return !(getLogic() != null ? !getLogic().equals(that.getLogic()) : that.getLogic() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getInstrument() != null ? getInstrument().hashCode() : 0);
        result = 31 * result + (getQuestionItem() != null ? getQuestionItem().hashCode() : 0);
        result = 31 * result + (getIndexRationale() != null ? getIndexRationale().hashCode() : 0);
        result = 31 * result + (getLogic() != null ? getLogic().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "ControlConstruct{" +
                "instrument=" + instrument +
                ", question=" + questionItem +
                ", indexRationale='" + indexRationale + '\'' +
                ", logic='" + logic + '\'' +
                ", pre#=" + getPreInstructions().size() + '\'' +
                ", post#=" + getPostInstructions().size() + '\'' +
                '}';
    }

}


