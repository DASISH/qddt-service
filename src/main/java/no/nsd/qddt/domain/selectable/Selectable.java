package no.nsd.qddt.domain.selectable;

import no.nsd.qddt.domain.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
@Entity
@Table(name = "SELECTABLE")
public class Selectable extends AbstractEntity {

    String name;

    SelectableType selectableType;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.DETACH})
    @OrderColumn(name="selected_idx")
    private List<UuidReference> selectedReferences =new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SelectableType getSelectableType() {
        return selectableType;
    }

    public void setSelectableType(SelectableType selectableType) {
        this.selectableType = selectableType;
    }

    public List<UuidReference> getSelectedReferences() {
        return selectedReferences;
    }

    public void setSelectedReferences(List<UuidReference> selectedReferences) {
        this.selectedReferences = selectedReferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Selectable)) return false;
        if (!super.equals(o)) return false;

        Selectable that = (Selectable) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return (selectableType == that.selectableType);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (selectableType != null ? selectableType.hashCode() : 0);
        return result;
    }
}
