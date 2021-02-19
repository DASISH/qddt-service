package no.nsd.qddt.domain.category.json;

import no.nsd.qddt.domain.ResponseCardinality;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.category.HierarchyLevel;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.responsedomain.Code;

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
public class CategoryJsonView {

    private UUID id;

    private String label;

    private String name;

    private Version version;

    @Embedded
    private ResponseCardinality inputLimit;

    @Enumerated(EnumType.STRING)
    private HierarchyLevel hierarchyLevel;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private Code code;

    private String format;

    private List<CategoryJsonView> children = new ArrayList<>();


    public CategoryJsonView() {
    }

    public CategoryJsonView(Category category) {
        setId(category.getId());
        setLabel(category.getLabel());
        setName(category.getName());
        setVersion(category.getVersion());
        setInputLimit(category.getInputLimit());
        setHierarchyLevel(category.getHierarchyLevel());
        setCategoryType(category.getCategoryType());
        setCode(category.getCode());
        setFormat(category.getFormat());
        setChildren(category.getChildren().stream().map(CategoryJsonView::new).collect(Collectors.toList()));
    }

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    private void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public ResponseCardinality getInputLimit() {
        return inputLimit;
    }

    private void setInputLimit(ResponseCardinality inputLimit) {
        this.inputLimit = inputLimit;
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
