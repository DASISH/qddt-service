package no.nsd.qddt.domain.responsedomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ResponseCardinality;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.category.HierarchyLevel;
import no.nsd.qddt.domain.classes.interfaces.IWebMenuPreview;
import no.nsd.qddt.domain.classes.pdf.PdfReport;
import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.StringTool.CapString;
import static no.nsd.qddt.utils.StringTool.IsNullOrEmpty;


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
public class ResponseDomain extends AbstractEntityAudit implements IWebMenuPreview {
    /**
    *   Can't have two responsedomain with the same template and the same name, unless they are based on
    */

    @JsonIgnore
    @OrderColumn(name="responsedomain_idx")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CODE", joinColumns = @JoinColumn(name="responsedomain_id",  referencedColumnName = "id"))
    private List<Code> codes = new ArrayList<>();

    // @JsonView(View.Edit.class)
    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    /**
     *   a link to a category root/group (template)
     *   the managed representation is never reused (as was intended),
     *   so we want to remove it when the responseDomain is removed. ->  CascadeType.REMOVE
     */
    // @JsonView(View.Edit.class)
    @ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="category_id")
    private Category managedRepresentation;

    // @JsonView(View.Edit.class)
    private String displayLayout;

    // @JsonView(View.Edit.class)
    @Enumerated(EnumType.STRING)
    private ResponseKind responseKind;

    /**
     * Allows the designation of the minimum and maximum number of responses allowed for this response domain.
     */
    // @JsonView(View.Edit.class)
    @Embedded
    private ResponseCardinality responseCardinality;


    public ResponseDomain(){
        super();
        description = "";
    }

    @Override
    @Column(nullable = false)
    public String getName() {
        return CapString(super.getName());
    }

    public String getDescription() {
        if (IsNullOrEmpty(description))
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

    protected void setResponseCardinality(ResponseCardinality responseCardinality) {
        this.responseCardinality = responseCardinality;
    }

    /**
     * Vocabulary for Display layout  would suffice with 'Horizontal' (default) vs. Vertical'.
     * @return DisplayLayout
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
        this.codes =  managedRepresentation.getCodes();
    }

    public Category getManagedRepresentation() {
        assert managedRepresentation != null;
        if (getCodes().size() > 0)
            managedRepresentation.setCodes( getCodes() );
        return managedRepresentation;
    }

    @JsonIgnore
    public List<Category> getManagedRepresentationFlatten(){
        return  getFlatManagedRepresentation( getManagedRepresentation() );
    }


    protected List<Category> getFlatManagedRepresentation(Category current){
        List<Category> retval = new ArrayList<>();
        if (current == null) return  retval;
        retval.add(current);
        current.getChildren().forEach(c->retval.addAll(getFlatManagedRepresentation(c)));
        return  retval;
    }

    public void setManagedRepresentation(Category managedRepresentation) {
        LOG.debug("setManagedRepresentation");
        setCodes( managedRepresentation.getCodes());
        this.managedRepresentation = managedRepresentation;
    }

    @Override
    protected void beforeInsert() {}

    @Override
    protected void beforeUpdate() {
        if (responseCardinality == null)
            setResponseCardinality(managedRepresentation.getInputLimit());
        if (managedRepresentation.getCategoryType() == CategoryType.MIXED){
            setName(String.format("Mixed [%s]", managedRepresentation.getChildren().stream().map(Category::getLabel).collect(Collectors.joining(" + "))));
        }
        if (StringTool.IsNullOrTrimEmpty( managedRepresentation.getLabel()))
            managedRepresentation.setLabel( getName() );
        managedRepresentation.setName(  managedRepresentation.getCategoryType().getName() +  "[" +  ((getId() != null) ? getId().toString() : getName())  + "]");
        if (managedRepresentation.getHierarchyLevel() == HierarchyLevel.GROUP_ENTITY)
            managedRepresentation.setDescription( managedRepresentation.getCategoryType().getDescription() );
        else
            managedRepresentation.setDescription( getDescription() );
        managedRepresentation.setChangeComment(getChangeComment());
        managedRepresentation.setChangeKind(getChangeKind());
        managedRepresentation.setXmlLang( getXmlLang());

        if(!getVersion().isModified()) {
            LOG.debug("onUpdate not run yet ♣♣♣ ");
        }
        managedRepresentation.setVersion(getVersion());
            LOG.debug("ResponseDomain PrePersist " + getName() + " - " + getVersion());

    }

    public List<Code> getCodes() {
        if (codes == null)
            codes = new ArrayList<>();
        return codes.stream().filter(c-> !c.isEmpty()).collect(Collectors.toList());
    }

    public void setCodes(List<Code> codes) {
        if (codes.stream().filter(c-> !c.isEmpty()).count() > 0)
            this.codes = codes;
    }

    @Override
    public XmlDDIFragmentBuilder<ResponseDomain> getXmlBuilder() {
        return new ResponseDomainFragmentBuilder(this);
    }


    @Override
    public void fillDoc(PdfReport pdfReport,String counter) {
        com.itextpdf.layout.element.Table table =
            new com.itextpdf.layout.element.Table(UnitValue.createPercentArray(new float[]{15.0F,70.0F,15.0F}))
                .setKeepTogether(true)
                .setKeepTogether( true )
                .setWidth(pdfReport.width100*0.8F)
                .setBorder(new DottedBorder( ColorConstants.GRAY,1))
                .setFontSize(10);
        table.addCell(new Cell(1,2)
            .add(new Paragraph( this.getName()))
            .setBorder(new DottedBorder(ColorConstants.GRAY,1)))
            .addCell(new Cell()
                .setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph( String.format("Version %s", getVersion()))));
        for (Category cat: getFlatManagedRepresentation(getManagedRepresentation()))
            if (cat.getCategoryType() == CategoryType.CATEGORY) {
                table.addCell( new Cell()
                    .setBorder( new DottedBorder( ColorConstants.GRAY, 1 ) ) );
                table.addCell( new Cell().add( new Paragraph( cat.getLabel() ) )
                    .setBorder( new DottedBorder( ColorConstants.GRAY, 1 ) ) );
                table.addCell( new Cell()
                    .setTextAlignment( TextAlignment.CENTER )
                    .add( new Paragraph( cat.getCode() != null ? cat.getCode().getValue() : cat.getCategoryType().name() ) )
                    .setBorder( new DottedBorder( ColorConstants.GRAY, 1 ) ) );
            } else {
                table.addCell( new Cell().add( new Paragraph( cat.getCategoryType().name() ) )
                    .setBorder( new DottedBorder( ColorConstants.GRAY, 1 ) )
                );
                table.addCell( new Cell( 1, 2 ).add( new Paragraph( cat.getLabel() ) )
                    .setBorder( new DottedBorder( ColorConstants.GRAY, 1 ) ) );
            }
        pdfReport.getTheDocument().add(table);
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
        return "{" +
            "\"id\":" + (getId() == null ? "null" : "\"" + getId() +"\"" ) + ", " +
            "\"name\":" + (getName() == null ? "null" : "\"" + getName() + "\"") + ", " +
            "\"description\":" + (description == null ? "null" : "\"" + description + "\"") + ", " +
            "\"displayLayout\":" + (displayLayout == null ? "null" : "\"" + displayLayout + "\"") + ", " +
            "\"responseKind\":" + (responseKind == null ? "null" : responseKind) + ", " +
            "\"responseCardinality\":" + (responseCardinality == null ? "null" : responseCardinality) + ", " +
            "\"managedRepresentation\":" + (managedRepresentation == null ? "null" : managedRepresentation) + ", " +
            "\"modified\":" + (getModified() == null ? "null" : "\"" + getModified()+ "\"" ) + " , " +
            "\"modifiedBy\":" + (getModifiedBy() == null ? "null" : getModifiedBy()) +
            "}";
    }

}
