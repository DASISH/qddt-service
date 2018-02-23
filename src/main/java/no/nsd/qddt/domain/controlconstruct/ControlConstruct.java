package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.controlconstructparameter.ResponseReference;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.othermaterial.OtherMaterialCC;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.universe.Universe;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instrument is the significant relation.
 * Instrument will be asked for all {@link QuestionItem} instances it has and the
 * metadata in this class will be used as visual condition for each {@link QuestionItem}.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "CONTROL_CONSTRUCT")
public class ControlConstruct extends AbstractEntityAudit {

    private String label;
    private String description;
    private String condition;
    private ControlConstructKind controlConstructKind;
    private SequenceKind sequenceKind;

    private Set<OtherMaterialCC> otherMaterials = new HashSet<>();
    private List<Universe> universe =new ArrayList<>(0);
    private List<ResponseReference> parameters = new ArrayList<>();
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>(0);

    //------------- Begin QuestionItem revision early bind "hack" ---------------
    /**
     * This field is to keep a reference from QI to RD
     * in order to backtrace usage with the help of Hibernate
     * but due to revision override cannot be used otherwise
     */
    private QuestionItem questionItemReferenceOnly;
    /**
     * This field will be populated with the correct version of a QI,
     * but should never be persisted.
     */
    private QuestionItem questionItem;
    /**
     * This field must be available "raw" in order to set and query
     * questionItem by ID
     */
    private UUID questionItemUUID;
    private Long questionItemRevision;
    //------------- End QuestionItem revision early bind "hack"------------------


    //------------- Begin Child elements with "enver hack" ----------------------
    private List<ControlConstruct> children = new ArrayList<>();
    private ControlConstruct parentReferenceOnly;
    private Integer parentIdx;
    //------------- End Child elements with "enver hack"  -----------------------
    private String parentIdxRationale;


    public ControlConstruct() {
    }

    @PrePersist
    @PreUpdate
    private void setDefaults(){
        if (controlConstructKind==null)
            controlConstructKind = ControlConstructKind.QUESTION_CONSTRUCT;
        if (sequenceKind == null)
            sequenceKind = SequenceKind.NA;
    }


    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }


    @Column(length = 3000)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }


    @Enumerated(EnumType.STRING)
    public ControlConstructKind getControlConstructKind() {
        return controlConstructKind;
    }
    public void setControlConstructKind(ControlConstructKind controlConstructKind) {
        this.controlConstructKind = controlConstructKind;
    }

    @Transient
    @JsonDeserialize
    @JsonSerialize
    public String getSequenceKind() {
        return getSequenceEnum().getName();
    }
    public void setSequenceKind(String name) {
        this.sequenceKind = SequenceKind.getEnum(name);
    }

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name="sequence_kind")
    public SequenceKind getSequenceEnum() {
        if (sequenceKind == null)
            sequenceKind = SequenceKind.NA;
        return sequenceKind;
    }
    public void setSequenceEnum(SequenceKind sequenceKind) {
        this.sequenceKind = sequenceKind;
    }


    @OneToMany(mappedBy = "parent" ,fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
    public Set<OtherMaterialCC> getOtherMaterials() {
        return otherMaterials;
    }
    public void setOtherMaterials(Set<OtherMaterialCC> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }
    public OtherMaterialCC addOtherMaterial(OtherMaterialCC otherMaterial) {
        otherMaterial.setParent( this );
        return  otherMaterial;
    }


    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @OrderColumn(name="universe_idx")
    public List<Universe> getUniverse() {
        return universe;
    }
    public void setUniverse(List<Universe> universe) {
        this.universe = universe;
    }



    //------------- Begin QuestionItem revision early bind "hack" ---------------
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "questionitem_id",insertable = false,updatable = false)
    public QuestionItem getQuestionItemReferenceOnly() {
        return questionItemReferenceOnly;
    }
    public void setQuestionItemReferenceOnly(QuestionItem questionItemReferenceOnly) {
        this.questionItemReferenceOnly = questionItemReferenceOnly;
    }


    @JsonSerialize
    @JsonDeserialize
    @Transient
    public QuestionItem getQuestionItem() {
        if (questionItemReferenceOnly != null && questionItem != null)
            questionItem.setConceptRefs(questionItemReferenceOnly.getConceptRefs());
        return questionItem;
    }
    public void setQuestionItem(QuestionItem question) {
        this.questionItem = question;
    }

    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="questionitem_id")
    public UUID getQuestionItemUUID() {
        return questionItemUUID;
    }
    private void setQuestionItemUUID(UUID questionItem) {
        questionItemUUID = questionItem;
    }


    @Column(name = "questionitem_revision")
    public Long getQuestionItemRevision() {
        return questionItemRevision;
    }
    public void setQuestionItemRevision(Long questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }
    //------------- End QuestionItem revision early bind "hack"------------------



    //------------- Begin Child elements with "enver hack" ----------------------
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "PARENT_ID")
    @OrderColumn(name = "PARENT_IDX")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "parentIdx")
    public List<ControlConstruct> getChildren() {
        return children;
    }
    public void setChildren(List<ControlConstruct> children) {
        this.children = children;
    }


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @Type(type="pg-uuid")
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    public ControlConstruct getParentReferenceOnly() {
        return parentReferenceOnly;
    }
    public void setParentReferenceOnly(ControlConstruct parentReferenceOnly) {
        this.parentReferenceOnly = parentReferenceOnly;
    }
    @JsonIgnore
    public void setParent(ControlConstruct newParent) {
        setField( "parentReferenceOnly", newParent );
    }


    // Ordered arrayList doesn't work with Enver FIX
    @JsonIgnore
    @Column(name="parent_idx", insertable = false,updatable = false)
    protected Integer getParentIdx() {
        return parentIdx;
    }
    protected void setParentIdx(Integer parentIdx) {
        this.parentIdx = parentIdx;
    }
    //------------- End Child elements with "enver hack"  -----------------------


    @Column(name="parent_idx_rationale",length = 300)
    public String getparentIdxRationale() {
        return parentIdxRationale;
    }
    public void setparentIdxRationale(String parentIdxRationale) {
        this.parentIdxRationale = parentIdxRationale;
    }



    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany
    public List<Instruction> getPreInstructions() {
        return getControlConstructInstructions().stream()
            .filter(i->i.getInstructionRank().equals(ControlConstructInstructionRank.PRE))
            .map(ControlConstructInstruction::getInstruction)
            .collect(Collectors.toList());
    }
    private void setPreInstructions(List<Instruction> preInstructions) {
        harvestInstructions( ControlConstructInstructionRank.PRE,preInstructions);
    }

    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany
    public List<Instruction> getPostInstructions() {
        return getControlConstructInstructions().stream()
            .filter(i->i.getInstructionRank().equals(ControlConstructInstructionRank.POST))
            .map(ControlConstructInstruction::getInstruction)
            .collect(Collectors.toList());
    }
    private void setPostInstructions(List<Instruction> postInstructions) {
        harvestInstructions( ControlConstructInstructionRank.POST,postInstructions);
    }


    @JsonIgnore
    @OrderColumn(name="instruction_idx")
    @OrderBy("instruction_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_INSTRUCTION",
        joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")})
    public List<ControlConstructInstruction> getControlConstructInstructions() {
         return controlConstructInstructions;
    }
    public void setControlConstructInstructions(List<ControlConstructInstruction> controlConstructInstructions) {
        this.controlConstructInstructions = controlConstructInstructions;
    }
    private void harvestInstructions(ControlConstructInstructionRank rank,List<Instruction> instructions) {
        if (controlConstructInstructions == null)
            controlConstructInstructions = new ArrayList<>();
        else
            controlConstructInstructions.removeIf( p->p.getInstructionRank().equals( rank ) );
        try {
            for (Instruction instruction : instructions) {
                ControlConstructInstruction cci = new ControlConstructInstruction();
                cci.setInstruction(instruction);
                cci.setInstructionRank(rank);
                this.controlConstructInstructions.add(cci);
            }
        }catch (Exception ex){
            LOG.error("harvestInstructions exception",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
    }


    @OrderColumn(name="controlconstruct_idx")
    @OrderBy("controlconstruct_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_PARAMETER",
        joinColumns = @JoinColumn(name="controlconstruct_id"))
    public List<ResponseReference> getParameters() {
        return parameters;
    }
    public void setParameters(List<ResponseReference> parameters) {
        this.parameters = parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstruct)) return false;
        if (!super.equals(o)) return false;

        ControlConstruct that = (ControlConstruct) o;

        if (!getControlConstructKind().equals(that.getControlConstructKind()))
            return false;
        if (getQuestionItem() != null ? !getQuestionItem().equals(that.getQuestionItem()) : that.getQuestionItem() != null)
            return false;
        return (getparentIdxRationale() != null ? getparentIdxRationale().equals(that.getparentIdxRationale()) : that.getparentIdxRationale() == null) && !(getCondition() != null ? !getCondition().equals(that.getCondition()) : that.getCondition() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getQuestionItem() != null ? getQuestionItem().hashCode() : 0);
        result = 31 * result + (getparentIdxRationale() != null ? getparentIdxRationale().hashCode() : 0);
        result = 31 * result + (getCondition() != null ? getCondition().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ControlConstruct{" +
                "Kind=" + getControlConstructKind() +'\'' +
                ", question=" + questionItem +'\'' +
                ", parentIdxRationale='" + parentIdxRationale + '\'' +
                ", condition='" + condition + '\'' +
                ", pre#=" + getPreInstructions().size() + '\'' +
                ", post#=" + getPostInstructions().size() + '\'' +
                '}';
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter) throws IOException {
        pdfReport.addHeader(this, "ControlConstruct " + counter);

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


}


