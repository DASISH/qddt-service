package no.nsd.qddt.utils.builders;


import no.nsd.qddt.domain.bcategory.Category;
import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.bcategory.CategoryType;

/**
 * @author Stig Norland
 */
public class CategoryBuilder {
    private String name;
    private String label;
    private HierarchyLevel hierarchyLevel = HierarchyLevel.ENTITY;
    private CategoryType categoryType = CategoryType.TEXT;

    public CategoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder setLabel(String label) {
        this.label = label;
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
        category.setHierarchyLevel(this.hierarchyLevel);
        category.setCategoryType(this.categoryType);
        return category;
    }

}
