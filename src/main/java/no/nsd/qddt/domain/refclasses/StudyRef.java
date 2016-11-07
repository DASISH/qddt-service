package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.study.Study;

/**
 * @author Stig Norland
 */
public class StudyRef extends Refs {

    private SurveyRef surveyRef;

    public StudyRef(Study study) {
        super(study);
        surveyRef = new SurveyRef(study.getSurveyProgram());
    }

    public SurveyRef getSurveyRef() {
        return surveyRef;
    }

    public void setSurveyRef(SurveyRef surveyRef) {
        this.surveyRef = surveyRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyRef)) return false;
        if (!super.equals(o)) return false;

        StudyRef studyRef = (StudyRef) o;

        return surveyRef != null ? surveyRef.equals(studyRef.surveyRef) : studyRef.surveyRef == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (surveyRef != null ? surveyRef.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StudyRef{" +
                "surveyRef=" + surveyRef +
                "} " + super.toString();
    }
}
