package no.nsd.qddt.utils.builders;


import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.code.Code;

/**
 * @author Stig Norland
 */
public class CategoryBuilder {
    private String name;
    private String label;
    private String code;
    private HierarchyLevel hierarchyLevel = HierarchyLevel.ENTITY;
    private CategoryType categoryType = CategoryType.CATEGORY;

    public CategoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public CategoryBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public CategoryBuilder setHierarchy(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
        return this;
    }

    public CategoryBuilder setType(CategoryType categoryType) {
        this.categoryType = categoryType;
        return this;
    }

    public Category createCategory() {
        Category category = new Category();
        category.setName(this.name);
        category.setLabel(this.label);
//        Code  aCode = new Code();
//        aCode.setCodeValue(this.code);
//        aCode.setResponseDomain();
//        category.setCode(aCode);
        category.setHierarchyLevel(this.hierarchyLevel);
        category.setCategoryType(this.categoryType);
        return category;
    }

}
