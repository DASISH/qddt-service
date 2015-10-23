package no.nsd.qddt.domain.bcategory;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.responsedomaincode.ResponseDomainCode;
import org.hibernate.annotations.Type;
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

    @OneToMany(mappedBy="category", cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

//    @ManyToOne
//    @JoinColumn(name="parent_id")
//    private Category parent;

//    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST} )
    @JoinTable(name = "CATEGORY_CATEGORY",
            joinColumns = {@JoinColumn(name ="id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "parent_id", nullable = false)})
    private Set<Category> children = new HashSet<>();


    //name -> A description of a particular category or response.

    @Column(name = "label")
    private String label;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "concept_reference", nullable = true)
    @Type(type="pg-uuid")
    private UUID conceptReference;

    @Column(name = "category_kind")
    @Enumerated(EnumType.STRING)
    private CategorySchemaType categorySchemaType;

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

    public CategorySchemaType getCategorySchemaType() {
        return categorySchemaType;
    }

    public void setCategorySchemaType(CategorySchemaType categorySchemaType) {
        this.categorySchemaType = categorySchemaType;
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

    public Set<ResponseDomainCode> getResponseDomainCodes() {
        return responseDomainCodes;
    }

    public void setResponseDomainCodes(Set<ResponseDomainCode> responseDomainCodes) {
        this.responseDomainCodes = responseDomainCodes;
    }

//    public Category getParent() {
//        return parent;
//    }
//
//    public void setParent(Category parent) {
//        this.parent = parent;
//    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public void addChild(Category children) {
        this.children.add(children);
//        children.setParent(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        if (!super.equals(o)) return false;

        Category category = (Category) o;
        if (description != null ? !description.equals(category.description) : category.description != null) return false;
        return !(this.label != null ? !this.label.equals(category.label) : category.label != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Code{" +
                " category='" + label + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }


}
