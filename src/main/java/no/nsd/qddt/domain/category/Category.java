package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.code.Code;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
@Table(name = "CATEGORY")
public class Category extends AbstractEntityAudit {

    @OneToOne(mappedBy="category", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Code code;

    @OneToMany(mappedBy="category", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<ResponseDomain> responseDomain = new HashSet<>();

    @OneToMany(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
//    @OrderColumn(name="category_idx")
    private Set<Category> children = new HashSet<>();


    //name -> A description of a particular category or response.

    /*
    A display label for the category.
    May be expressed in multiple languages.
    Repeat for labels with different content, for example,
    labels with differing length limitations or of different types or applications.
     */
    @Column(name = "label")
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
    @Column(name = "input_limit", nullable = true)
    @Embedded
    private ResponseCardinality inputLimit;


    @Column(name = "classification_level", nullable = true)
    @Enumerated(EnumType.STRING)
    private CategoryRelationCodeType classificationLevel;


    /**
     * concept reference to a versioned concept within the system.
     */
    @Column(name = "concept_reference", nullable = true)
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
    private String categoryJsonDDI;

    public Category() {

    }

    /***
     *
     * @param name Category name
     * @param label Shorter version of name if applicable
     */
    public Category(String name, String label){
        setName(name);
        setLabel(label);
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
        if (classificationLevel == null) {
            switch (categoryType) {
                case MISSING_GROUP:
                case LIST:
                    setClassificationLevel(CategoryRelationCodeType.Ordinal);
                    break;
                case SCALE:
                    setClassificationLevel(CategoryRelationCodeType.Interval);
                    break;
                case MIXED:
                    setClassificationLevel(CategoryRelationCodeType.Continuous);
                    break;
                default:
//                setClassificationLevel(CategoryRelationCodeType.Nominal);
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
    }

    public String getDescription() {
        return description;
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

    public Set<ResponseDomain> getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(Set<ResponseDomain> responseDomain) {
        this.responseDomain = responseDomain;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public void addChild(Category children) {
        this.children.add(children);
    }

    public Set<Category> getAllChildrenFlatten(){
        Set<Category> grandchildren = new HashSet<>();

        for(Category c:this.getChildren()){
            if (c.getHierarchyLevel() == HierarchyLevel.ENTITY)
                grandchildren.add(c);
            else
                grandchildren.addAll(c.getAllChildrenFlatten());
        }
        return grandchildren;
    }

    public ResponseCardinality getInputLimit() {
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

    public String getCategoryJsonDDI() {
        return categoryJsonDDI;
    }

    public void setCategoryJsonDDI(String categoryJsonDDI) {
        this.categoryJsonDDI = categoryJsonDDI;
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
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", hierarchyLevel=" + hierarchyLevel +
                ", categoryType=" + categoryType +
                "} " + super.toString();
    }

    public boolean fieldCompare(Category o) {

        if (code != null && !code.equals(o.code)) return false;
        if (responseDomain != null && !responseDomain.equals(o.responseDomain)) return false;
        if (children != null && !children.equals(o.children)) return false;
        if (label != null && !label.equals(o.label)) return false;
        if (description != null && !description.equals(o.description)) return false;
        if (hierarchyLevel != null && !hierarchyLevel.equals(o.hierarchyLevel)) return false;
        if (categoryType != null && !categoryType.equals(o.categoryType)) return false;
        if (categoryJsonDDI != null && !categoryJsonDDI.equals(o.categoryJsonDDI)) return false;

        return super.fieldCompare(o);

    }


}
