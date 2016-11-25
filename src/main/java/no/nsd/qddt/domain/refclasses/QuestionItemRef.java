package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.questionItem.QuestionItem;

import java.util.*;

/**
 * @author Stig Norland
 */
public class QuestionItemRef extends Refs {

//    private Map<UUID,ConceptRef> conceptRefs;

    private Set<ConceptRef> conceptRefs;

    public QuestionItemRef(){
        super();
        conceptRefs = new HashSet<>(0);
    }

    public QuestionItemRef(QuestionItem entity) {
        super(entity);
        conceptRefs = new HashSet<>(entity.getConceptRefs());
//        conceptRefs.putAll(entity.getConceptRefs());
    }



    public Set<ConceptRef> getConceptRefs() {
        return conceptRefs;
    }

    public void setConceptRefs(Set<ConceptRef> conceptRefs) {
        this.conceptRefs = conceptRefs;
    }

//    public Map<UUID, ConceptRef> getConceptRefs() {
//        return conceptRefs;
//    }
//
//    public void setConceptRefs(Map<UUID, ConceptRef> conceptRefs) {
//        this.conceptRefs = conceptRefs;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionItemRef)) return false;
        if (!super.equals(o)) return false;

        QuestionItemRef that = (QuestionItemRef) o;

        return conceptRefs != null ? conceptRefs.equals(that.conceptRefs) : that.conceptRefs == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (conceptRefs != null ? conceptRefs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "QuestionRef{" +
                "conceptRefs=" + conceptRefs +
                "} " + super.toString();
    }
}
