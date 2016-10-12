package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
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
    @OrderColumn(name = "instrument_index")
    @ManyToOne
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "CC_PRE_INSTRUCTION",
            joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "instruction_id", referencedColumnName = "id")})
    @OrderColumn(name="preInstructions_idx")
    private List<Instruction> preInstructions =new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "CC_POST_INSTRUCTION",
            joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "instruction_id", referencedColumnName = "id")})
    @OrderColumn(name="posInstructions_idx")
    private List<Instruction> postInstructions =new ArrayList<>();


    private String logic;

    @OneToMany
    private List<CCParameter> inParameter = new ArrayList<>();

    @OneToMany
    private List<CCParameter> outParameter = new ArrayList<>();

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

    public List<CCParameter> getInParameter() {
        return inParameter;
    }

    public void setInParameter(List<CCParameter> in) {
        this.inParameter = in;
    }

    public List<CCParameter> getOutParameter() {
        return outParameter;
    }

    public void setOutParameter(List<CCParameter> out) {
        this.outParameter = out;
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


