package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.study.Study;

/**
 * @author Stig Norland
 */
public class StudyRef extends BaseRef<Study> {

    public StudyRef(Study study) {
        super(study);
    }
    @Override
    public String toString() {
        return "StudyRef{" +
                "surveyRef=" + getParentRef() +
                "} " + super.toString();
    }

}
