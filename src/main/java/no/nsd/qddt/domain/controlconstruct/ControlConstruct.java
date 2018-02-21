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

    //------------- Begin QuestionItem revision early bind "hack" ---------------

    /**
     * This field is to keep a reference from QI to RD
     * in order to backtrace usage with the help of Hibernate
     * but due to revision override cannot be used otherwise
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "questionitem_id",insertable = false,updatable = false)
    private QuestionItem questionItemReferenceOnly;

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
    private Long questionItemRevision;

    //------------- End QuestionItem revision early bind "hack"------------------



    //------------- Begin Child elements with "enver hack" ----------------------

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "parent_idx")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "parent_idx")
    private List<ControlConstruct> children = new ArrayList<>();


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @Type(type="pg-uuid")
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private ControlConstruct parentReferenceOnly;


    // Ordered arrayList doesn't work with Enver FIX
    @Column(insertable = false,updatable = false)
    private Integer parent_idx;

    //------------- End Child elements with "enver hack"  -----------------------


    @Column(name="idx_rationale",length = 300)
    private String indexRationale;

    private String label;

    @Column(length = 3000)
    private String description;


    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @OrderColumn(name="universe_idx")
    private List<Universe> universe =new ArrayList<>(0);


    @OneToMany(mappedBy = "parent" ,fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
//    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    private Set<OtherMaterialCC> otherMaterials = new HashSet<>();


    @JsonIgnore
    @OrderColumn(name="instruction_idx")
    @OrderBy("instruction_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_INSTRUCTION",
                    joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")})
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>(0);


    private String condition;


    @OrderColumn(name="controlconstruct_idx")
    @OrderBy("controlconstruct_idx ASC")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONTROL_CONSTRUCT_PARAMETER",
            joinColumns = @JoinColumn(name="controlconstruct_id"))
    private List<ResponseReference> parameters = new ArrayList<>();


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

    @Enumerated(EnumType.STRING)
    private ControlConstructKind controlConstructKind;

    @Enumerated(EnumType.STRING)
    private SequenceKind sequenceKind;

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

    public QuestionItem getQuestionItem() {
        if (questionItemReferenceOnly != null && questionItem != null)
            questionItem.setConceptRefs(questionItemReferenceOnly.getConceptRefs());
        return questionItem;
    }
    public void setQuestionItem(QuestionItem question) {
        this.questionItem = question;
    }

    public Long getQuestionItemRevision() {
        return questionItemRevision;
    }
    public void setQuestionItemRevision(Long questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

    public UUID getQuestionItemUUID() {
        return questionItemUUID;
    }
    private void setQuestionItemUUID(UUID questionItem) {
        questionItemUUID = questionItem;
    }

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

    public List<Universe> getUniverse() {
        return universe;
    }
    public void setUniverse(List<Universe> universe) {
        this.universe = universe;
    }

    public List<ControlConstruct> getChildren() {
        return children;
    }
    public void setChildren(List<ControlConstruct> children) {
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
    public void populateControlConstructInstructions() {
        if (controlConstructInstructions == null)
            controlConstructInstructions = new ArrayList<>();
        else
            controlConstructInstructions.clear();

        harvestInstructions(ControlConstructInstructionRank.PRE, getPreInstructions());
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
            .collect(Collectors.toList()));

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

    public String getIndexRationale() {
        return indexRationale;
    }

    public void setIndexRationale(String indexRationale) {
        this.indexRationale = indexRationale;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<ResponseReference> getParameters() {
        return parameters;
    }

    public void setParameters(List<ResponseReference> parameters) {
        this.parameters = parameters;
    }

    public ControlConstructKind getControlConstructKind() {
        return controlConstructKind;
    }

    public void setControlConstructKind(ControlConstructKind controlConstructKind) {
        this.controlConstructKind = controlConstructKind;
    }

    public String getSequenceKind() {
        return getSequenceEnum().getName();
    }

    public void setSequenceKind(String name) {
        this.sequenceKind = SequenceKind.getEnum(name);
    }

    @JsonIgnore
    public SequenceKind getSequenceEnum() {
        if (sequenceKind == null)
            sequenceKind = SequenceKind.NA;
        return sequenceKind;
    }

    public void setSequenceEnum(SequenceKind sequenceKind) {
        this.sequenceKind = sequenceKind;
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
        return (getIndexRationale() != null ? getIndexRationale().equals(that.getIndexRationale()) : that.getIndexRationale() == null) && !(getCondition() != null ? !getCondition().equals(that.getCondition()) : that.getCondition() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getQuestionItem() != null ? getQuestionItem().hashCode() : 0);
        result = 31 * result + (getIndexRationale() != null ? getIndexRationale().hashCode() : 0);
        result = 31 * result + (getCondition() != null ? getCondition().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ControlConstruct{" +
                "Kind=" + getControlConstructKind() +'\'' +
                ", question=" + questionItem +'\'' +
                ", indexRationale='" + indexRationale + '\'' +
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

/*     @Override
    public void makeNewCopy(Long revision) {
        if (hasRun) return;
        super.makeNewCopy( revision );
        getChildren().forEach( c -> {
            c.makeNewCopy( revision );
            c.setParent( this );
        } );
//        getOtherMaterials().forEach( m -> {
//            m.makeNewCopy( revision );
//            m.setParent( this.getId() );
//        } );
        getComments().clear();
    } */


    public void setParent(ControlConstruct newParent) {
        setField( "parentReferenceOnly", newParent );
    }


}


