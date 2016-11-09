package no.nsd.qddt.domain.refclasses;

import no.nsd.qddt.domain.surveyprogram.SurveyProgram;

public class SurveyRef extends Refs{


    public SurveyRef(){
        super();
    }

    public SurveyRef(SurveyProgram surveyProgram){
        super(surveyProgram);
    }

}