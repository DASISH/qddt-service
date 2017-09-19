package no.nsd.qddt.domain.category.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryRelationCodeType;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.embedded.ResponseCardinality;
import no.nsd.qddt.domain.responsedomain.Code;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class CategoryJsonEdit extends BaseJsonEdit {

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

    private List<CategoryJsonEdit> children = new ArrayList<>();


    public CategoryJsonEdit() {
    }

    public CategoryJsonEdit(Category category) {
        super(category);
        setChildren(category.getChildren().stream().map(CategoryJsonEdit::new).collect(Collectors.toList()));
        setLabel(category.getLabel());
        setDescription(category.getDescription());
        setInputLimit(category.getInputLimit());
        setClassificationLevel(category.getClassificationLevel());
        setHierarchyLevel(category.getHierarchyLevel());
        setCategoryType(category.getCategoryType());
        setCode(category.getCode());
    }

    public String getLabel() {
        return label;
    }

    private void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public ResponseCardinality getInputLimit() {
        return inputLimit;
    }

    private void setInputLimit(ResponseCardinality inputLimit) {
        this.inputLimit = inputLimit;
    }

    public CategoryRelationCodeType getClassificationLevel() {
        return classificationLevel;
    }

    private void setClassificationLevel(CategoryRelationCodeType classificationLevel) {
        this.classificationLevel = classificationLevel;
    }

    public HierarchyLevel getHierarchyLevel() {
        return hierarchyLevel;
    }

    private void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    private void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Code getCode() {
        return code;
    }

    private void setCode(Code code) {
        this.code = code;
    }

    public List<CategoryJsonEdit> getChildren() {
        return children;
    }

    private void setChildren(List<CategoryJsonEdit> children) {
        this.children = children;
    }

}
