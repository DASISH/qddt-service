package no.nsd.qddt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "ChangeReason")
public class ChangeReason extends AbstractEntity{

    @Column(name = "Label")
    private String label;

    @Column(name = "Description")
    private  String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ChangeReason that = (ChangeReason) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChangeReason{" +
                "label='" + label + "' : '" + description + '\'' +
                '}';
    }
}
