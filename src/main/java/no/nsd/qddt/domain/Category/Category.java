package no.nsd.qddt.domain.Category;

import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Stig Norland
 */
@Entity
//@NamedNativeQuery(name = "findUniqueCategoryInOrder", query= "select distinct category as name from Code  order by category", resultClass= Category.class)
@Subselect(value = "select distinct category as name, row_number() OVER () as id from Code  order by category")
public class Category  implements java.io.Serializable {

    @Id
    private Long id;


    @Column(name = "name", nullable = false,unique = true,insertable = false, updatable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        return !(getName() != null ? !getName().equals(category.getName()) : category.getName() != null);

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}
