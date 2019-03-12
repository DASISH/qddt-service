package no.nsd.qddt.domain.refclasses;

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
}