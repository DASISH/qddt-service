package no.nsd.qddt.domain.parentref;

import no.nsd.qddt.domain.surveyprogram.SurveyProgram;

public class SurveyRef extends BaseRef<SurveyRef>{


    public SurveyRef(){
        super();
    }

    public SurveyRef(SurveyProgram surveyProgram){
        super(surveyProgram);
    }

    @Override
    public int compareTo(SurveyRef o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public IRefs getParent() {
        return null;
    }
}