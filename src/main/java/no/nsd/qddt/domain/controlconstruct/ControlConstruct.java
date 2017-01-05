package no.nsd.qddt.domain.controlconstruct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.commentable.Commentable;
import no.nsd.qddt.domain.controlconstructinstruction.ControlConstructInstruction;
import no.nsd.qddt.domain.controlconstructinstruction.InstructionRank;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.parameter.CCParameter;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instrument is the significant relation.
 * Instrument will be asked for all {@link Question} instances it has and the
 * metadata in this class will be used as visual condition for each {@link Question}.
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "CONTROL_CONSTRUCT")
public class ControlConstruct extends AbstractEntityAudit  implements Commentable{

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

    // TODO remove this element after frontend updated
    @Column(name = "questionitem_revision")
    private Integer revisionNumber;

    @Column(name = "questionitem_revision", insertable = false,updatable = false)
    private Integer questionItemRevision;

    //------------- End QuestionItem revision early bind "hack"------------------



    //------------- Begin Child elements with "enver hack" ----------------------

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "parent_idx")
    // Ordered arrayList doesn't work with Enver FIX
    @AuditMappedBy(mappedBy = "parentReferenceOnly", positionMappedBy = "parent_idx")
    private List<ControlConstruct> children = new ArrayList<>();

//    // Ordered arrayList doesn't work with Enver FIX
//    @Type(type="pg-uuid")
//    @Column(name= "parent_id", insertable = false,updatable = false)
//    private UUID parent;


    @JsonBackReference(value = "parentRef")
    @ManyToOne()
    @JoinColumn(name = "parent_id",updatable = false,insertable = false)
    private ControlConstruct parentReferenceOnly;


    // Ordered arrayList doesn't work with Enver FIX
    @Column(insertable = false,updatable = false)
    private Integer parent_idx;

    //------------- End Child elements with "enver hack"  -----------------------


    //------------- Begin Instrument with "enver hack" --------------------------

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    // Ordered arrayList doesn't work with Enver FIX
    @Column(insertable = false,updatable = false)
    private Integer instrument_idx;

    //------------- End Instrument with "enver hack" ----------------------------

    @Column(length = 300)
    private String indexRationale;

    private String label;

    @Column(length = 3000)
    private String description;


    @OneToMany(mappedBy = "owner" ,fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "controlConstruct",fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.DETACH})
    @OrderColumn(name="instructions_idx")
    private List<ControlConstructInstruction> controlConstructInstructions =new ArrayList<>();

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

    @Transient
    @JsonSerialize
    @JsonDeserialize
    @OneToMany(mappedBy = "ownerId" ,fetch = FetchType.EAGER, cascade =CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();


    public ControlConstruct() {
    }

    @PrePersist
    @PreUpdate
    private void setDefaults(){
        if (controlConstructKind==null)
            controlConstructKind = ControlConstructKind.QUESTION_CONSTRUCT;
//        System.out.println("setDefaults -> " + controlConstructKind);
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

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
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
        System.out.println("populateControlConstructInstructions");
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

    public Set<Comment> getComments() {
        return comments;
    }


    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    public void addComment(Comment comment) {
        comment.setOwnerId(this.getId());
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlConstruct)) return false;
        if (!super.equals(o)) return false;

        ControlConstruct that = (ControlConstruct) o;

        if (!getControlConstructKind().equals(that.getControlConstructKind()))
            return false;
        if (getInstrument() != null ? !getInstrument().equals(that.getInstrument()) : that.getInstrument() != null)
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


}


