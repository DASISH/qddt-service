package no.nsd.qddt.domain.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.utils.builders.StringTool;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.*;

/**
 *
 * CategoryScheme : Categories provide enumerated representations for
 *      concepts and are used by questions, category lists, and variables
 *
 * CodeListScheme : Code lists link a specific value with a category and
 *      are used by questions and variables
 *
 * ManagedRepresentationScheme : Reusable representations of numeric,
 *      textual datetime, scale or missing values types.
 *
 * CodeType (aka Code) A structure that links a unique value of a category to a
 * specified category and provides information as to the location of the category
 * within a hierarchy, whether it is discrete, represents a total for the CodeList contents,
 * and if its sub-elements represent a comprehensive coverage of the category.
 * The Code is identifiable, but the value within the category must also be unique within the CodeList.
 *
 *
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Audited
@Entity
@Table(name = "CATEGORY", uniqueConstraints = {@UniqueConstraint(columnNames = {"label","name","category_kind","based_on_object"},name = "UNQ_CATEGORY_NAME_KIND")})
public class Category extends AbstractEntityAudit  implements Comparable<Category>{

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Code code;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @OrderColumn(name="category_idx")
//    @OrderBy("category_idx ASC")
    private List<Category> children = new ArrayList<>();


    //name -> A description of a particular category or response.

    /*
    A display label for the category.
    May be expressed in multiple languages.
    Repeat for labels with different content, for example,
    labels with differing length limitations or of different types or applications.
     */
    @Column(name = "label")
    @OrderBy()
    private String label;

    /*
    A description of the content and purpose of the category.
    May be expressed in multiple languages and supports the use of structured content.
    Note that comparison of categories is done using the content of description.
     */
    @Column(name = "description", length = 2000)
    private String description;

    /**
     * This field is only used for categories that facilitates user input.
     * like numeric range / text length /
     */
    @Column(name = "input_limit")
    @Embedded
    private ResponseCardinality inputLimit;


    @Column(name = "classification_level")
    @Enumerated(EnumType.STRING)
    private CategoryRelationCodeType classificationLevel;

    /**
     * format is used by datetime, and other kinds if needed.
     */
    private String format;

    /**
     * concept reference to a versioned concept within the system.
     */
    @Column(name = "concept_reference")
    @Type(type="pg-uuid")
    private UUID conceptReference;

    @Column(name = "Hierarchy_level",nullable = false)
    @Enumerated(EnumType.STRING)
    private HierarchyLevel hierarchyLevel;

    @Column(name = "category_kind", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;


    /*
    different kind of categories have different metatags that are used in other tools,
    We'll store them in a textfield and let the gui do the magic.
     i.e.  datetime format
     */
//    private String categoryJsonDDI;

    public Category() {
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        if (StringTool.IsNullOrTrimEmpty(getName()))
            setName(label.toUpperCase());
    }

    public String getDescription() {
        return StringTool.CapString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getConceptReference() {
        return conceptReference;
    }

    public void setConceptReference(UUID conceptReference) {
        this.conceptReference = conceptReference;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
            this.code = code;
    }

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

    public void setClassificationLevel(CategoryRelationCodeType classificationLevel) {
        this.classificationLevel = classificationLevel;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Category> getChildren() {
        return  children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public void addChild(Category children) {
        this.children.add(children);
    }

     @Override
     @Column(nullable = false)
     public String getName(){
        if (StringTool.IsNullOrTrimEmpty(super.getName()))
            return this.getLabel().toUpperCase();
        else
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
        if (getConceptReference() != null ? !getConceptReference().equals(category.getConceptReference()) : category.getConceptReference() != null)
            return false;
        return getHierarchyLevel() == category.getHierarchyLevel();

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getLabel() != null ? getLabel().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getConceptReference() != null ? getConceptReference().hashCode() : 0);
        result = 31 * result + (getHierarchyLevel() != null ? getHierarchyLevel().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                ", label='" + getLabel() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", hierarchyLevel=" + hierarchyLevel +
                ", categoryType=" + categoryType +
                "} " + super.toString();
    }

    public boolean fieldCompare(Category o) {

        if (children != null && !children.equals(o.children)) return false;
        if (label != null && !label.equals(o.label)) return false;
        if (description != null && !description.equals(o.description)) return false;
        if (hierarchyLevel != null && !hierarchyLevel.equals(o.hierarchyLevel)) return false;
        if (categoryType != null && !categoryType.equals(o.categoryType)) return false;

        return super.fieldCompare(o);

    }

    /*
    preRec for valid Categories
     */
    @JsonIgnore
    protected  boolean isValid(){
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
}
