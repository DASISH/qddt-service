package no.nsd.qddt.domain;

import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Stig Norland
 */
@Entity
@NamedNativeQuery(name = "findUniqueHashTagInOrder", query = "select * from HashTag order by name", resultClass = HashTag.class)
public class HashTag {

    private String Name;

    @Id
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return Name;
    }

    private void setName(String name) {
        Name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashTag hashTag = (HashTag) o;

        if (Name != null ? !Name.equals(hashTag.Name) : hashTag.Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Name != null ? Name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HashTag{" +
                "Name='" + Name + '\'' +
                '}';
    }
}
