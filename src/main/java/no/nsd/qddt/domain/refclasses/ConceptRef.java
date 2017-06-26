package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.concept.Concept;

/**
 * @author Stig Norland
 */
public class ConceptRef extends BaseRef<ConceptRef> {

    private TopicRef parent;

    public ConceptRef() {
        super();
    }

    public ConceptRef(Concept concept){
        super(concept);

        parent = concept.getTopicRef();
    }

    public TopicRef getTopicRef() {
        return parent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptRef)) return false;
        if (!super.equals(o)) return false;

        ConceptRef that = (ConceptRef) o;

        return parent != null ? parent.equals(that.parent) : that.parent == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConceptRef{" +
                "topicRef=" + parent +
                "} " + super.toString();
    }

    @Override
    public int compareTo(ConceptRef o) {
        int i =  this.getTopicRef().getStudyRef().compareTo(o.getTopicRef().getStudyRef());
        if (i==0)
            i =  this.getTopicRef().compareTo(o.getTopicRef());
        if (i==0)
            i= getName().compareToIgnoreCase(o.getName());
        return i;
    }
}
