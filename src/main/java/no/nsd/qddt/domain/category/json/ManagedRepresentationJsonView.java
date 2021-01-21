package no.nsd.qddt.domain.category.json;

import no.nsd.qddt.domain.classes.ResponseCardinality;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryRelationCodeType;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.category.HierarchyLevel;

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
public class ManagedRepresentationJsonView {

    private UUID id;

    private String label;

    @Embedded
    private ResponseCardinality inputLimit;

    @Enumerated(EnumType.STRING)
    private CategoryRelationCodeType classificationLevel;

    @Enumerated(EnumType.STRING)
    private HierarchyLevel hierarchyLevel;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private String format;

    private List<CategoryJsonView> children = new ArrayList<>();


    public ManagedRepresentationJsonView() {
    }

    public ManagedRepresentationJsonView(Category category) {
        this.id = category.getId();
        setLabel( category.getLabel() );
        setChildren(category.getChildren().stream().map(CategoryJsonView::new).collect(Collectors.toList()));
        setInputLimit(category.getInputLimit());
        setClassificationLevel(category.getClassificationLevel());
        setHierarchyLevel(category.getHierarchyLevel());
        setCategoryType(category.getCategoryType());
        setFormat(category.getFormat());
    }

    public UUID getId() {
        return this.id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<CategoryJsonView> getChildren() {
        return children;
    }

    private void setChildren(List<CategoryJsonView> children) {
        this.children = children;
    }

}
