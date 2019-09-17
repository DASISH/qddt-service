package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.questionitem.QuestionItem;

import java.util.*;

/**
 * @author Stig Norland
 */
public class QuestionItemRef extends BaseRef<QuestionItemRef> {

    private ConceptRef parent;
    private List<ConceptRef> parents;

    public QuestionItemRef(QuestionItem entity) {
        super(entity);
        parents = entity.getConceptRefs();
        parent = entity.getConceptRefs().get( 0 );
    }

    @Override
    public ConceptRef getParent() {
        return parent;
    }

    public List<ConceptRef> getParents() {
        return parents;
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

    @Override
    public int compareTo(QuestionItemRef o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

}
