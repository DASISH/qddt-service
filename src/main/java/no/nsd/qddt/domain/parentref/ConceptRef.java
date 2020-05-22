package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.concept.Concept;

/**
 * @author Stig Norland
 */
public class ConceptRef extends BaseRef<Concept> {

    public ConceptRef(Concept concept){
        super(concept);
    }

    @Override
    public String toString() {
        return "ConceptRef{" +
                "topicRef=" + getParentRef() +
                "} " + super.toString();
    }

}
