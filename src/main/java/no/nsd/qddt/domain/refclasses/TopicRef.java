package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.topicgroup.TopicGroup;

/**
 * @author Stig Norland
 */


public class TopicRef extends Refs{

    StudyRef studyRef;

    public TopicRef(TopicGroup topicGroup) {
        super(topicGroup);
        studyRef = new StudyRef(topicGroup.getStudy());
    }

    public StudyRef getStudyRef() {
        return studyRef;
    }

    public void setStudyRef(StudyRef studyRef) {
        this.studyRef = studyRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicRef)) return false;
        if (!super.equals(o)) return false;

        TopicRef topicRef = (TopicRef) o;

        return studyRef != null ? studyRef.equals(topicRef.studyRef) : topicRef.studyRef == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (studyRef != null ? studyRef.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicRef{" +
                "studyRef=" + studyRef +
                "} " + super.toString();
    }
}
