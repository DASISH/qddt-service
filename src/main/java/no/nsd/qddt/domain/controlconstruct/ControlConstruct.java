package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
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

    @ManyToOne
    @JoinColumn(name = "questionitem_id")
    private QuestionItem questionItem;

    @OneToMany(fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();


    @OneToMany(mappedBy="controlConstruct", cascade = CascadeType.ALL)
    private Set<Instruction> preInstructions = new HashSet<>();

    @OneToMany(mappedBy="controlConstruct", cascade = CascadeType.ALL)
    private Set<Instruction> postInstructions = new HashSet<>();



    private String logic;

    @Column(length = 3000)
    // TODO remove this?
    private String instruction;


    public ControlConstruct() {
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public Set<Instruction> getPreInstructions() {
        return preInstructions;
    }

    public void setPreInstructions(Set<Instruction> preInstructions) {
        this.preInstructions = preInstructions;
    }

    public Set<Instruction> getPostInstructions() {
        return postInstructions;
    }

    public void setPostInstructions(Set<Instruction> postInstructions) {
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


