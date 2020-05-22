package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.topicgroup.TopicGroup;

/**
 * @author Stig Norland
 */


public class TopicRef extends BaseRef<TopicGroup>{


    public TopicRef(TopicGroup topicGroup) {
        super(topicGroup);
    }

    @Override
    public String toString() {
        return "TopicRef{" +
                "studyRef=" + getParentRef() +
                "} " + super.toString();
    }

}
