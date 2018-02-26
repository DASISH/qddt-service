package no.nsd.qddt.domain.category.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryRelationCodeType;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.embedded.ResponseCardinality;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ManagedRepresentationJsonEdit extends BaseJsonEdit {

    private String label;
    private String description;
    private ResponseCardinality inputLimit;
    private CategoryRelationCodeType classificationLevel;
    private HierarchyLevel hierarchyLevel;
    private CategoryType categoryType;
    private String format;
    private List<CategoryJsonEdit> children = new ArrayList<>();


    public ManagedRepresentationJsonEdit() {
    }

    public ManagedRepresentationJsonEdit(Category category) {
        super(category);
        setChildren(category.getChildren().stream().map(CategoryJsonEdit::new).collect(Collectors.toList()));
        setLabel(category.getLabel());
        setDescription(category.getDescription());
        setInputLimit(category.getInputLimit());
        setClassificationLevel(category.getClassificationLevel());
        setHierarchyLevel(category.getHierarchyLevel());
        setCategoryType(category.getCategoryType());
        setFormat(category.getFormat());
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


    @Embedded
    public ResponseCardinality getInputLimit() {
        return inputLimit;
    }
    private void setInputLimit(ResponseCardinality inputLimit) {
        this.inputLimit = inputLimit;
    }


    @Enumerated(EnumType.STRING)
    public CategoryRelationCodeType getClassificationLevel() {
        return classificationLevel;
    }
    private void setClassificationLevel(CategoryRelationCodeType classificationLevel) {
        this.classificationLevel = classificationLevel;
    }


    @Enumerated(EnumType.STRING)
    public HierarchyLevel getHierarchyLevel() {
        return hierarchyLevel;
    }
    private void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }


    @Enumerated(EnumType.STRING)
    public CategoryType getCategoryType() {
        return categoryType;
    }
    private void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }

    public List<CategoryJsonEdit> getChildren() {
        return children;
    }
    private void setChildren(List<CategoryJsonEdit> children) {
        this.children = children;
    }

}
