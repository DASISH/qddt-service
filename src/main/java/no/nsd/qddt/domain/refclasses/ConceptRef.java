package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.concept.Concept;

/**
 * @author Stig Norland
 */
public class ConceptRef extends Refs {

    private TopicRef topicRef;

    public ConceptRef() {
        super();
    }

    public ConceptRef(Concept concept){
        super(concept);
        topicRef = new TopicRef(concept.getTopicGroup());
    }

    public TopicRef getTopicRef() {
        return topicRef;
    }

    public void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptRef)) return false;
        if (!super.equals(o)) return false;

        ConceptRef that = (ConceptRef) o;

        return topicRef != null ? topicRef.equals(that.topicRef) : that.topicRef == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (topicRef != null ? topicRef.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConceptRef{" +
                "topicRef=" + topicRef +
                "} " + super.toString();
    }
}
