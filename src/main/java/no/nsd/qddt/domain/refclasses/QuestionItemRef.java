package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.questionItem.QuestionItem;

import java.util.*;

/**
 * @author Stig Norland
 */
public class QuestionItemRef extends BaseRef {

    private Set<ConceptRef> parents;

    public QuestionItemRef(){
        super();

    }

    public QuestionItemRef(QuestionItem entity) {
        super(entity);
        parents = new HashSet<>(entity.getConceptRefs());
    }


    public Set<ConceptRef> getConceptRefs() {
        return parents;
    }

    public void setConceptRefs(Set<ConceptRef> conceptRefs) {
        this.parents = conceptRefs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionItemRef)) return false;
        if (!super.equals(o)) return false;

        QuestionItemRef that = (QuestionItemRef) o;

        return parents != null ? parents.equals(that.parents) : that.parents == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parents != null ? parents.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionRef{" +
                "conceptRefs=" + parents +
                "} " + super.toString();
    }
}
