package no.nsd.qddt.domain.responsedomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.kernel.color.ColorConstants;
import com.itextpdf.layout.border.DottedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.refclasses.QuestionItemRef;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * CodeList A special form of maintainable that allows a single codelist to be maintained outside of a CodeListScheme.
 *
 *<dl>
 * <dt>QuestionGrid</dt><dd>Structures the QuestionGrid as an NCube-like structure providing dimension information, labeling options, and response domains attached to one or more cells within the grid.</dd>
 * <dt>QuestionItem</dt><dd>Structure a single Question which may contain one or more response domains (i.e., a list of valid category responses where if "Other" is indicated a text response can be used to specify the intent of "Other").</dd>
 * <dt>ResponseInMixed</dt><dd>A structure that provides both the response domain and information on how it should be attached, or related, to other specified response domains in the question.</dd>
 *
 * <dt>Category</dt><dd>A category (without an attached category) response for a question item.</dd>
 *      <dd>Implemented as: Code.Category = Code.codeValue;</dd>
 *
 * <dt>Code</dt><dd>A coded response (where both codes and their related category value are displayed) for a question item.</dd>
 *      <dd>Implemented as:  Code.Category = "A_NAME", Code.CodeValue = "A_VALUE"</dd>
 *
 * <dt>Numeric</dt><dd>A numeric response (the intent is to analyze the response as a number) for a question item.</dd>
 *      <dd>Implemented as:  Code = NULL; (no category is needed)</dd>
 *
 * <dt>Scale</dt><dd>A scale response which describes a 1..n dimensional scale of various display types for a question.</dd>
 *      <dd>Implemented as: Code.CodeValue = valid values 1..n + control codes (N/A, Refuses, can't answer, don't know etc)</dd>
 *
 * <dt>Text</dt><dd>A textual response.</dd>
 *      <dd>Implemented as: Code = NULL; (no category is needed)</dd>
 *
 * <dt>These to be implemented later </dt>
 *      <dd>-DateTime;    A date or time response for a question item.</dd>
 *      <dd>-Distribution;A distribution response for a question, may only be included in-line.</dd>
 *      <dd>-Geographic;  A geographic coordinate reading as a response for a question item.</dd>
 *      <dd>-GeographicLocationCode; A response domain capturing the name or category of a Geographic Location as a response for a question item, may only be included in-line.</dd>
 *      <dd>-GeographicStructureCode;A geographic structure category as a response for a question item, may only be included in-line.</dd>
 *      <dd>-Location;    A location response (mark on an image, recording, or object) for a question, may only be included in-line.</dd>
 *      <dd>-Nominal;     A nominal (check off) response for a question grid response, may only be included in-line.</dd>
 *      <dd>-Ranking;     A ranking response which supports a "ranking" of categories. Generally used within a QuestionGrid, may only be included in-line.</dd>
 *</dl>
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "RESPONSEDOMAIN", uniqueConstraints = {@UniqueConstraint(columnNames = {"name","category_id","based_on_object"},name = "UNQ_RESPONSEDOMAIN_NAME")})         //also -> based_on_object?
public class ResponseDomain extends AbstractEntityAudit  {
    /**
    *   Can't have two responsedomain with the same template and the same name, unless they are based on
    */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responseDomain",cascade = {CascadeType.DETACH})
    private Set<QuestionItem> questionItems = new HashSet<>();

    
    @JsonIgnore
    @OrderColumn(name="responsedomain_idx")
    @OrderBy("responsedomain_idx ASC")
    @ElementCollection
    @CollectionTable(name = "CODE",
    joinColumns = @JoinColumn(name="responsedomain_id"))
    private List<Code> codes = new ArrayList<>();

    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    /**
     *   a link to a category root/group (template)
     *   the managed representation is never reused (as was intended),
     *   so we want to remove it when the responseDomain is removed. ->  CascadeType.REMOVE
     */
    @ManyToOne(cascade = { CascadeType.MERGE ,  CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category managedRepresentation;

    private String displayLayout;

    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    /**
     * Allows the designation of the minimum and maximum number of responses allowed for this response domain.
     */
    @Embedded
    private ResponseCardinality responseCardinality;


    public ResponseDomain(){
        description = "";
    }

    @Override
    @Column(nullable = false)
    public String getName() {
        return StringTool.CapString(super.getName());
    }

    public String getDescription() {
        if (StringTool.IsNullOrEmpty(description))
            description= "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResponseKind getResponseKind() {
        return responseKind;
    }

    public void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    public ResponseCardinality getResponseCardinality() {
        if (responseCardinality == null)
            return new ResponseCardinality();
        return responseCardinality;
    }

    private void setResponseCardinality(ResponseCardinality responseCardinality) {
        this.responseCardinality = responseCardinality;
    }


    public Set<QuestionItem> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(Set<QuestionItem> questionItems) {
        this.questionItems = questionItems;
    }

    /**
     * Vocabulary for Display layout  would suffice with 'Horizontal' (default) vs. Vertical'.
     * @return
     */
    public String getDisplayLayout() {
        return displayLayout;
    }

    public void setDisplayLayout(String displayLayout) {
        this.displayLayout = displayLayout;
    }

    /**
    *    this is useful for populating codes before saving to DB
    */
    public void populateCodes() {
        this.codes.clear();
        harvestCatCodes(managedRepresentation);
    }

    private void harvestCatCodes(Category current){
        if (current == null) return;
        if (current.getHierarchyLevel() == HierarchyLevel.ENTITY) {

            if (getId()== null && getResponseKind() == ResponseKind.MIXED) {
                Code code = new Code(current.getCode().getCodeValue());
                this.codes.add(code);
            } else {
                Code code = current.getCode();
                this.codes.add(code);
            }
        }
        current.getChildren().forEach(this::harvestCatCodes);
    }

    @Transient
    private int _Index;    // /used to keep track of current item in the recursive call populateCatCodes

    /**
     * this function is useful for populating managedRepresentation after loading from DB
     */
    private void populateCatCodes(Category current){
        assert current != null;
        if (current.getHierarchyLevel() == HierarchyLevel.ENTITY ) {
            try {
                Code code = codes.get(_Index);
                current.setCode(code);
                _Index++;
            } catch (IndexOutOfBoundsException iob){
                current.setCode(new Code());
            } catch(Exception ex) {
                LOG.error(DateTime.now().toDateTimeISO()+
                        " populateCatCodes (catch & continue) " + ex.getMessage()+ " - " +
                        current);
                current.setCode(new Code());
            }
        }
        current.getChildren().forEach(this::populateCatCodes);
    }


    public Category getManagedRepresentation() {
        assert managedRepresentation != null;
        _Index = 0;
        populateCatCodes(managedRepresentation);
        return managedRepresentation;
    }

    private List<Category> getFlatManagedRepresentation(Category current){
        List<Category> retval = new ArrayList<>();
        if (current == null) return  retval;
        retval.add(current);
        current.getChildren().forEach(c->retval.addAll(getFlatManagedRepresentation(c)));
        return  retval;
    }

    public void setManagedRepresentation(Category managedRepresentation) {
        LOG.debug("setManagedRepresentation");
        this.codes.clear();
        harvestCatCodes(managedRepresentation);
        this.managedRepresentation = managedRepresentation;
//        beforeUpdate();
    }

    @Override
    protected void beforeUpdate() {
        if (responseCardinality == null)
            setResponseCardinality(managedRepresentation.getInputLimit());
        if (managedRepresentation.getCategoryType() == CategoryType.MIXED){
            setName(String.format("Mixed [%s]", managedRepresentation.getChildren().stream().map(Category::getName).collect(Collectors.joining(" + "))));
        }
        managedRepresentation.setName(getName());
        managedRepresentation.setDescription(String.format("[%s] group - %s",
                StringTool.CapString(managedRepresentation.getCategoryType().name().toLowerCase()),
                getDescription()));
        managedRepresentation.setChangeComment("");
        managedRepresentation.setChangeKind(getChangeKind());
        if(!getVersion().isModified()) {
            LOG.debug("onUpdate not run yet ♣♣♣ ");
//            onUpdate();
        }

        managedRepresentation.setVersion(getVersion());
            LOG.debug("ResponseDomain PrePersist " + getName() + " - " + getVersion());

    }

    public List<Code> getCodes() {
        if (codes == null)
            codes = new ArrayList<>();
        return codes.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }


    @Transient
    @JsonSerialize
    @JsonDeserialize
    public Set<QuestionItemRef> getQuestionRefs(){
        return  new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseDomain)) return false;
        if (!super.equals(o)) return false;

        ResponseDomain that = (ResponseDomain) o;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return getResponseKind() == that.getResponseKind();

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (responseKind != null ? responseKind.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {

        return MessageFormat.format("ResponseDomain { {0}, {1}, {2} } " ,
                super.toString(),
                getCodes(),
                getManagedRepresentation().toString());
    }

    @Override
    public void fillDoc(PdfReport pdfReport,String counter) throws IOException {
        com.itextpdf.layout.element.Table table =
            new com.itextpdf.layout.element.Table(UnitValue.createPercentArray(new float[]{15.0F,70.0F,15.0F}))
                .setKeepTogether(true)
                .setWidthPercent(80)
                .setBorder(new DottedBorder(ColorConstants.GRAY,1))
                .setFontSize(10);
        table.addCell(new Cell(1,2)
            .add(this.getName())
            .setBorder(new DottedBorder(ColorConstants.GRAY,1)))
            .addCell(new Cell()
                .setTextAlignment(TextAlignment.RIGHT)
                .add("Version " +this.getVersion().toString()));
        for (Category cat: getFlatManagedRepresentation(getManagedRepresentation())) {
            if (cat.getHierarchyLevel() == HierarchyLevel.ENTITY ){
                table.addCell(new Cell()
                        .setBorder(new DottedBorder(ColorConstants.GRAY,1)));
                table.addCell(new Cell().add(cat.getLabel())
                        .setBorder(new DottedBorder(ColorConstants.GRAY,1)));
                table.addCell(new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .add(cat.getCode()!=null ? cat.getCode().getCodeValue(): cat.getCategoryType().name())
                    .setBorder(new DottedBorder(ColorConstants.GRAY,1)));
            } else {
                table.addCell(new Cell().add(cat.getCategoryType().name())
                        .setBorder(new DottedBorder(ColorConstants.GRAY,1))
                        );
                table.addCell(new Cell(1,2).add(cat.getName())
                    .setBorder(new DottedBorder(ColorConstants.GRAY,1)));
            }
        }
        pdfReport.getTheDocument().add(table);
    }

    @Override
    public void makeNewCopy(Integer revision){
        if (hasRun) return;
        super.makeNewCopy(revision);
        managedRepresentation.makeNewCopy(revision);
        getComments().clear();
    }
}