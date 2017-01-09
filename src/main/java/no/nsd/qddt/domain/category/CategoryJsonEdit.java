package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.responsedomain.Code;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class CategoryJsonEdit extends BaseJsonEdit {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private List<CategoryJsonEdit> children = new ArrayList<>();

    private String label;

    private String description;

    @Embedded
    private ResponseCardinality inputLimit;

    @Enumerated(EnumType.STRING)
    private CategoryRelationCodeType classificationLevel;

    @Enumerated(EnumType.STRING)
    private HierarchyLevel hierarchyLevel;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private Code code;



    public CategoryJsonEdit() {
    }

    public CategoryJsonEdit(Category category) {
        super(category);
        setId(category.getId());
        setName(category.getName());
        setChildren(category.getChildren().stream().map(F->new CategoryJsonEdit(F)).collect(Collectors.toList()));
        setLabel(category.getLabel());
        setDescription(category.getDescription());
        setInputLimit(category.getInputLimit());
        setClassificationLevel(category.getClassificationLevel());
        setHierarchyLevel(category.getHierarchyLevel());
        setCategoryType(category.getCategoryType());
        setCode(category.getCode());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryJsonEdit> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryJsonEdit> children) {
        this.children = children;
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

    public ResponseCardinality getInputLimit() {
        return inputLimit;
    }

    public void setInputLimit(ResponseCardinality inputLimit) {
        this.inputLimit = inputLimit;
    }

    public CategoryRelationCodeType getClassificationLevel() {
        return classificationLevel;
    }

    public void setClassificationLevel(CategoryRelationCodeType classificationLevel) {
        this.classificationLevel = classificationLevel;
    }

    public HierarchyLevel getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
