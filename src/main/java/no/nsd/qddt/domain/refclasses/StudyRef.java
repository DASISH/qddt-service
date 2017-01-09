package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.study.Study;

/**
 * @author Stig Norland
 */
public class StudyRef extends BaseRef {

    private SurveyRef parent;

    public StudyRef(){
        super();
        parent = new SurveyRef();
    }


    public StudyRef(Study study) {
        super(study);
        parent = new SurveyRef(study.getSurveyProgram());
    }

    public SurveyRef getSurveyRef() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyRef)) return false;
        if (!super.equals(o)) return false;

        StudyRef studyRef = (StudyRef) o;

        return parent != null ? parent.equals(studyRef.parent) : studyRef.parent == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StudyRef{" +
                "surveyRef=" + parent +
                "} " + super.toString();
    }
}
