package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.concept.Concept;

/**
 * @author Stig Norland
 */
public class ConceptRef extends BaseRef<ConceptRef> {

    private TopicRef parent;

        public ConceptRef(Concept concept){
        super(concept);
        parent = concept.getTopicRef();
    }


    @Override
    public TopicRef getParent() {
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
        if (o == null)
            return  -1;
        int i;
        try {
            i = this.getParent().getParent().compareTo(o.getParent().getParent());
            if (i == 0)
                i = this.getParent().compareTo(o.getParent());
            if (i == 0)
                i = getName().compareToIgnoreCase(o.getName());
        } catch (NullPointerException ex) {
            i = -1;
        }
        return i;
    }


}
