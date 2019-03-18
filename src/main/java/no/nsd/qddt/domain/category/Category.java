package no.nsd.qddt.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.pdf.PdfReport;
import no.nsd.qddt.domain.responsedomain.Code;
import no.nsd.qddt.domain.xml.AbstractXmlBuilder;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.StringTool.SafeString;

/**
 *
 * <p>CategoryScheme : Categories provide enumerated representations for
 *      concepts and are used by questions, category lists, and variables</p>
 *
 * <p>CodeListScheme : Code lists link a specific value with a category and
 *      are used by questions and variables</p>
 *
 * <p>ManagedRepresentationScheme : Reusable representations of numeric,
 *      textual datetime, scale or missing values types.</p>
 *
 * <p>CodeType (aka Code) A structure that links a unique value of a category to a
 * specified category and provides information as to the location of the category
 * within a hierarchy, whether it is discrete, represents a total for the CodeList contents,
 * and if its sub-elements represent a comprehensive coverage of the category.
 * The Code is identifiable, but the value within the category must also be unique within the CodeList.</p>
 *
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "CATEGORY", uniqueConstraints = {@UniqueConstraint(columnNames = {"label","name","category_kind" },name = "UNQ_CATEGORY_NAME_KIND")})   //https://github.com/DASISH/qddt-client/issues/606
public class Category extends AbstractEntityAudit  implements Comparable<Category> , Cloneable {

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Code code;

    @ManyToMany(fetch = FetchType.EAGER)
//    @Cascade( {org.hibernate.annotations.CascadeType.SAVE_UPDATE} )
    @OrderColumn(name="category_idx")
    private List<Category> children = new ArrayList<>();


    @Column(name = "label")
    @OrderBy()
    private String label;

    @Column(name = "description", length = 2000)
    private String description;


    @Column(name = "input_limit")
    @Embedded
    private ResponseCardinality inputLimit;


    @Column(name = "classification_level")
    @Enumerated(EnumType.STRING)
    private CategoryRelationCodeType classificationLevel;

    private String format;


    @Column(name = "Hierarchy_level",nullable = false)
    @Enumerated(EnumType.STRING)
    private HierarchyLevel hierarchyLevel;

    @Column(name = "category_kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    public Category() {
        super();
        code = new Code();
        hierarchyLevel = HierarchyLevel.ENTITY;
        setCategoryType(CategoryType.CATEGORY);
        setInputLimit("0","1");
    }

    /***
     *
     * @param name Category name
     * @param label Shorter version of name if applicable
     */
    public Category(String name, String label){
        this();
        setName(name);
        setLabel(label);
    }

    public CategoryType getCategoryType() {
        if (categoryType == null)
            setCategoryType(CategoryType.CATEGORY);
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
        if (classificationLevel == null) {
            switch (categoryType) {
                case MISSING_GROUP:
                case LIST:
                    setClassificationLevel(CategoryRelationCodeType.Ordinal);
                    setHierarchyLevel(HierarchyLevel.GROUP_ENTITY);
                    break;
                case SCALE:
                    setClassificationLevel(CategoryRelationCodeType.Interval);
                    setHierarchyLevel(HierarchyLevel.GROUP_ENTITY);
                    break;
                case MIXED:
                    setClassificationLevel(CategoryRelationCodeType.Continuous);
                    setHierarchyLevel(HierarchyLevel.GROUP_ENTITY);
                    break;
                default:
                    setHierarchyLevel(HierarchyLevel.ENTITY);
                    break;
            }
        }
    }

    public HierarchyLevel getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    /*
    A display label for the category.
    May be expressed in multiple languages.
    Repeat for labels with different content, for example,
    labels with differing length limitations or of different types or applications.
     */
    public String getLabel() {
        return SafeString(label);
    }

    public void setLabel(String label) {
        this.label = label;
        if (StringTool.IsNullOrTrimEmpty(super.getName()))
            setName(label.toUpperCase());
    }

    /*
   A description of the content and purpose of the category.
   May be expressed in multiple languages and supports the use of structured content.
   Note that comparison of categories is done using the content of description.
    */
    public String getDescription() {
        return StringTool.CapString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
            this.code = code;
    }

    /**
     * This field is only used for categories that facilitates user input.
     * like numeric range / text length /
     */
    public ResponseCardinality getInputLimit() {
        if (inputLimit == null)
            setInputLimit("0","1");
        return inputLimit;
    }

    public void setInputLimit(ResponseCardinality inputLimit) {
        this.inputLimit = inputLimit;
    }

    public void setInputLimit(String minimum, String maximum) {
        this.inputLimit = new ResponseCardinality(minimum,maximum) ;
    }

    public CategoryRelationCodeType getClassificationLevel() {
        return classificationLevel;
    }

    private void setClassificationLevel(CategoryRelationCodeType classificationLevel) {
        this.classificationLevel = classificationLevel;
    }

    /**
     * format is used by datetime, and other kinds if needed.
     */
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Category> getChildren() {
        if (categoryType == CategoryType.SCALE) {
            if (children == null || children.size() == 0)
                LOG.error("getChildren() is 0/NULL");
            return children.stream().filter( Objects::nonNull )
                    .sorted( Comparator.comparing( Category::getCode ) )
                    .collect( Collectors.toList() );
        } else
            return  children.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void setChildren(List<Category> children) {
        if (categoryType == CategoryType.SCALE)
            this.children = children.stream().sorted(Comparator.comparing(Category::getCode)).collect(Collectors.toList());
        this.children = children;
    }


    @Override
    @Column(nullable = false)
    public String getName(){
        if (StringTool.IsNullOrTrimEmpty(super.getName()))
            super.setName(this.getLabel().toUpperCase());
        return super.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        if (!super.equals(o)) return false;

        Category category = (Category) o;

        if (getLabel() != null ? !getLabel().equals(category.getLabel()) : category.getLabel() != null) return false;
        if (getDescription() != null ? !getDescription().equals(category.getDescription()) : category.getDescription() != null)
            return false;
        return getHierarchyLevel() == category.getHierarchyLevel();

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getLabel() != null ? getLabel().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getHierarchyLevel() != null ? getHierarchyLevel().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"Category\":"
            + super.toString()
            + ", \"code\":" + code
            + ", \"children\": [" + children.stream().map( c -> c.toString() ).collect( Collectors.joining(", ") ) + "]"
            + ", \"label\":\"" + label + "\""
            + ", \"description\":\"" + description + "\""
            + ", \"inputLimit\":" + inputLimit
            + ", \"classificationLevel\":\"" + classificationLevel + "\""
            + ", \"format\":\"" + format + "\""
            + ", \"hierarchyLevel\":\"" + hierarchyLevel + "\""
            + ", \"categoryType\":\"" + categoryType + "\""
            + ", \"_Index\":\"" + _Index + "\""
            + "}";
    }


    @Override
    public void fillDoc(PdfReport pdfReport,String counter) {
        Document document =pdfReport.getTheDocument();
        switch (getCategoryType()){
            case DATETIME:
            case TEXT:
            case NUMERIC:
            case BOOLEAN:
            case URI:
            case CATEGORY:
                document.add(new Paragraph("Category " + getLabel()));
                document.add(new Paragraph("Type " + getCategoryType().name()));
                break;
            case MISSING_GROUP:
                break;
            case LIST:
                break;
            case SCALE:
                break;
            case MIXED:
                break;
        }

        document.add(new Paragraph(" " ));
    }


    /*
    preRec for valid Categories
     */
    @JsonIgnore
    boolean isValid(){
        if (hierarchyLevel== HierarchyLevel.ENTITY)
            switch (categoryType) {
                case DATETIME:
                case TEXT:
                case NUMERIC:
                case BOOLEAN:
                case URI:
                    return (children.size() == 0 && inputLimit.isValid());
                case CATEGORY:
                    return (children.size() == 0
                            && label != null && !label.trim().isEmpty()
                            && getName() != null && !getName().trim().isEmpty());
                default:
                    return false;
            }
        else // hierarchyLevel== HierarchyLevel.GROUP_ENTITY)
            switch (categoryType) {
                case MISSING_GROUP:
                case LIST:
                    return (children.size() > 0 && inputLimit.isValid() && classificationLevel != null);
                case SCALE:
                    return (children.size() >= 2  && inputLimit.isValid() && classificationLevel != null);
                case MIXED:
                    return (children.size() >= 2  && classificationLevel != null);
                default:
                    return false;
            }
    }

    @Override
    public int compareTo(Category o) {

        int i;
        i = this.getAgency().compareTo(o.getAgency());
        if (i!=0) return i;

        i = this.getHierarchyLevel().compareTo(o.getHierarchyLevel());
        if (i!=0) return i;

        i = this.getCategoryType().compareTo(o.getCategoryType());
        if (i!=0) return i;

        i = this.getName().compareTo(o.getName());
        if (i!=0) return i;

        i = this.getLabel().compareTo(o.getLabel());
        if (i!=0) return i;

        i = this.getDescription().compareTo(o.getDescription());
        if (i!=0) return i;

        i= this.getId().compareTo(o.getId());
        if (i!=0) return i;

        return super.getModified().compareTo(o.getModified());

    }

    @Override
    protected void beforeUpdate() {
        LOG.debug("Category beforeUpdate " + getName());
        if (inputLimit == null)
            setInputLimit("0","1");
        beforeInsert();
    }

    @Override
    protected void beforeInsert() {
        LOG.debug("Category beforeInsert " + getName());
        if (getCategoryType() == null)
            setCategoryType(CategoryType.CATEGORY);
        if (getHierarchyLevel() == null)
            switch (getCategoryType()) {
                case DATETIME:
                case BOOLEAN:
                case URI:
                case TEXT:
                case NUMERIC:
                case CATEGORY:
                    setHierarchyLevel(HierarchyLevel.ENTITY);
                    break;
                case MISSING_GROUP:
                case LIST:
                case SCALE:
                case MIXED:
                    setHierarchyLevel(HierarchyLevel.GROUP_ENTITY);
                    break;
            }
    }

    // /used to keep track of current item in the recursive call populateCatCodes
    @Transient
    private int _Index;
    // this is useful for populating codes before saving to DB (used in the service)
    @JsonIgnore
    public List<Code> getCodes() {
     return harvestCatCodes(this);
    }
    public  void setCodes(List<Code> codes) {
     _Index =0;
     populateCatCodes( this, codes );
     //codes.clear();
    }

    private List<Code> harvestCatCodes(Category current){
     List<Code> tmplist = new ArrayList<>( 0);
     if (current == null) return tmplist;
     if (current.getHierarchyLevel() == HierarchyLevel.ENTITY && current.getCode()!=null) {
         tmplist.add( current.getCode()==null ? new Code(""): current.getCode() );
     }
     current.getChildren().forEach(c->  tmplist.addAll(harvestCatCodes(c)));
     return tmplist;
    }

    private void populateCatCodes(Category current,List<Code> codes){
     assert current != null;
     if (current.getHierarchyLevel() == HierarchyLevel.ENTITY) {
         try {
             current.setCode(codes.get(_Index));
             _Index++;
         } catch (IndexOutOfBoundsException iob){
             current.setCode(new Code());
         } catch(Exception ex) {
             LOG.error( DateTime.now().toDateTimeISO()+
                 " populateCatCodes (catch & continue) " + ex.getMessage()+ " - " +
                 current);
             current.setCode(new Code());
         }
     }
     current.getChildren().forEach( c -> populateCatCodes( c, codes ) );
 }


    @Override
    public Category clone() {
        Category clone = new Category(getName(),label);
        clone.setCategoryType(categoryType);
        clone.setClassificationLevel(classificationLevel);
        clone.setChildren(children);
        clone.setCode(code);
        clone.setDescription(description);
        clone.setFormat(format);
        clone.setHierarchyLevel(hierarchyLevel);
        clone.setInputLimit(inputLimit);
        clone.setBasedOnObject(getId());
        clone.setChangeKind(ChangeKind.NEW_COPY);
        clone.setChangeComment("Copy of [" + getName() + "]");
        return clone;
    }

    @Override
    public AbstractXmlBuilder getXmlBuilder() {
        return new CategoryFragmentBuilder(this);
    }

}
