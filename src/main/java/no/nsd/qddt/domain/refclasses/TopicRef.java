package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.topicgroup.TopicGroup;

/**
 * @author Stig Norland
 */


public class TopicRef extends BaseRef<TopicRef>{

    StudyRef parent;

    public TopicRef(){
        super();
        parent = new StudyRef();
    }


    public TopicRef(TopicGroup topicGroup) {
        super(topicGroup);
        parent = new StudyRef(topicGroup.getStudy());
    }

    public StudyRef getStudyRef() {
        return parent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicRef)) return false;
        if (!super.equals(o)) return false;

        TopicRef topicRef = (TopicRef) o;

        return parent != null ? parent.equals(topicRef.parent) : topicRef.parent == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicRef{" +
                "studyRef=" + parent +
                "} " + super.toString();
    }

    @Override
    public int compareTo(TopicRef o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
}
