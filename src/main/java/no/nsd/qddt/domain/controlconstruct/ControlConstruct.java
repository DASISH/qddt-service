package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstructinstruction.ControlConstructInstruction;
import no.nsd.qddt.domain.controlconstructinstruction.InstructionRank;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.parameter.CCParameter;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questionitem_id")
    private QuestionItem questionItem;

    @Column(name = "questionitem_revision")
    private Long questionItemRevision;


    @OneToMany(fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    @OneToMany(mappedBy = "controlConstruct",fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @OrderColumn(name="instructions_idx")
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>();


    private String logic;

    @OneToMany
    private List<CCParameter> parameters = new ArrayList<>();

    public ControlConstruct() {
    }

    public Long getQuestionItemRevision() {
        return questionItemRevision;
    }

    public void setQuestionItemRevision(Long questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
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

    public QuestionItem getQuestionItem() {
        return questionItem;
    }

    public void setQuestionItem(QuestionItem question) {
        this.questionItem = question;
    }

    public Set<ControlConstruct> getChildren() {
        return children;
    }

    public void setChildren(Set<ControlConstruct> children) {
        this.children = children;
    }

    @JsonIgnore
    public List<ControlConstructInstruction> getControlConstructInstructions() {
        return controlConstructInstructions;
    }

    public void setControlConstructInstructions(List<ControlConstructInstruction> controlConstructInstructions) {
        this.controlConstructInstructions = controlConstructInstructions;
    }

        public List<Instruction> getPreInstructions() {
        return controlConstructInstructions.stream()
                .filter(i->i.getInstructionRank().equals(InstructionRank.PRE))
                .map(ControlConstructInstruction::getInstruction)
                .collect(Collectors.toList());
    }


    public void addPreInstructions(Instruction preInstruction) {
        ControlConstructInstruction cci = new ControlConstructInstruction();
        cci.setInstruction(preInstruction);
        cci.setInstructionRank(InstructionRank.PRE);
        cci.setControlConstruct(this);
        this.controlConstructInstructions.add(cci);
    }

    public List<Instruction> getPostInstructions() {
        return controlConstructInstructions.stream()
                .filter(i->i.getInstructionRank().equals(InstructionRank.POST))
                .map(ControlConstructInstruction::getInstruction)
                .collect(Collectors.toList());
    }

    public void setPostInstructions(Instruction postInstruction) {
        ControlConstructInstruction cci = new ControlConstructInstruction();
        cci.setInstruction(postInstruction);
        cci.setInstructionRank(InstructionRank.POST);
        cci.setControlConstruct(this);
        this.controlConstructInstructions.add(cci);
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
        return "InstrumentQuestion{" +
                "instrument=" + instrument +
                ", question=" + questionItem +
                ", indexRationale='" + indexRationale + '\'' +
                ", logic='" + logic + '\'' +
                '}';
    }
}


