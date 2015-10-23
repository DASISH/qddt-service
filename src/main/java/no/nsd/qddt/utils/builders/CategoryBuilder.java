package no.nsd.qddt.utils.builders;


import no.nsd.qddt.domain.bcategory.Category;
import no.nsd.qddt.domain.bcategory.CategorySchemaType;

/**
 * @author Stig Norland
 */
public class CategoryBuilder {
    private String name;
    private String tag;
    private CategorySchemaType categorySchemaType;

    public CategoryBuilder setCategory(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder setLabel(String label) {
        this.tag = label;
        return this;
    }

    public CategoryBuilder setType(CategorySchemaType categorySchemaType) {
        this.categorySchemaType = categorySchemaType;
        return this;
    }



    public Category createCode() {
        Category category = new Category();
        category.setName(this.name);
        category.setTag(tag);
        category.setCategorySchemaType(categorySchemaType);
        return category;
    }

}
