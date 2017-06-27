package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.parameter.CCParameter;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
    private Integer questionItemRevision;

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
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private ControlConstruct parentReferenceOnly;


    // Ordered arrayList doesn't work with Enver FIX
    @Column(insertable = false,updatable = false)
    private Integer parent_idx;

    //------------- End Child elements with "enver hack"  -----------------------


    //------------- Begin Instrument with "enver hack" --------------------------


    @JsonBackReference(value = "instrumentRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "controlConstructs")
    private Set<Instrument> instruments = new HashSet<>();


    //------------- End Instrument with "enver hack" ----------------------------

    @Column(name="idx_rationale",length = 300)
    private String indexRationale;

    private String label;

    @Column(length = 3000)
    private String description;


    @OneToMany(mappedBy = "owner" ,fetch = FetchType.EAGER, cascade =CascadeType.REMOVE)
//    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @NotAudited
    private Set<OtherMaterial> otherMaterials = new HashSet<>();


    @JsonIgnore
    @OrderColumn(name="instruction_idx")
    @OrderBy("instruction_idx ASC")
    @ElementCollection
    @CollectionTable(name = "CONTROL_CONSTRUCT_INSTRUCTION",
                    joinColumns = {@JoinColumn(name = "control_construct_id", referencedColumnName = "id")})
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>(0);


    private String condition;

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

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public void addInstruments(Instrument instrument) {
        if (!instruments.contains(instrument))
            instrument.addControlConstruct(this);
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
//        System.out.println("populateControlConstructInstructions");
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
                cci.setInstructionRank(ControlConstructInstructionRank.POST);
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
                cci.setInstructionRank(ControlConstructInstructionRank.PRE);
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
//        System.out.println("populateQuestionItems " + getControlConstructInstructions().size());
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<CCParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<CCParameter> parameters) {
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
        if (getInstruments() != null ? !getInstruments().equals(that.getInstruments()) : that.getInstruments() != null)
            return false;
        if (getQuestionItem() != null ? !getQuestionItem().equals(that.getQuestionItem()) : that.getQuestionItem() != null)
            return false;
        if (getIndexRationale() != null ? !getIndexRationale().equals(that.getIndexRationale()) : that.getIndexRationale() != null)
            return false;
        return !(getCondition() != null ? !getCondition().equals(that.getCondition()) : that.getCondition() != null);

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
    public void fillDoc(PdfReport pdfReport) throws IOException {
        Document document =pdfReport.getTheDocument();
        document.add(new Paragraph()
            .add("ControlConstruct (" + this.controlConstructKind.name() + ")"));
        document.add(new Paragraph()
                .add("Name")
                .add(new Tab())
                .add(getName()));
        document.add(new Paragraph()
                .add("Pre Instructions"));
        for(Instruction pre:getPreInstructions()){
            document.add(new Paragraph()
                    .add(pre.getDescription()));
        }
        getQuestionItem().fillDoc(pdfReport);
        document.add(new Paragraph()
                .add("Post Instructions"));
        for(Instruction post:getPostInstructions()){
            document.add(new Paragraph()
                    .add(post.getDescription()));
        }
        pdfReport.addFooter(this);
    }

    @Override
    public void makeNewCopy(Integer revision) {
        if (hasRun) return;
        super.makeNewCopy(revision);
        getChildren().forEach(c->c.makeNewCopy(revision));
        getOtherMaterials().forEach(o->o.makeNewCopy(this.getId()));
        getComments().clear();
    }

}


