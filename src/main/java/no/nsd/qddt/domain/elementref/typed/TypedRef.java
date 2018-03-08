package no.nsd.qddt.domain.elementref.typed;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.publication.Publication;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;

/**
 * @author Stig Norland
 */
public class TypedRef {

    public static ElementRefTyped<?> cast(ElementRef element){
        switch (element.getElementKind()) {
            case SURVEY_PROGRAM:
                return  (ElementRefTyped<SurveyProgram>)element;
            case STUDY:
                return  (ElementRefTyped<Study>)element;
            case TOPIC_GROUP:
                return  (ElementRefTyped<TopicGroup>)element;
            case CONCEPT:
                return  (ElementRefTyped<Concept>)element;
            case QUESTION_ITEM:
                return  (ElementRefTyped<QuestionItem>)element;
            case RESPONSEDOMAIN:
                return  (ElementRefTyped<ResponseDomain>)element;
            case INSTRUMENT:
                return  (ElementRefTyped<Instrument>)element;
            case PUBLICATION:
                return  (ElementRefTyped<Publication>)element;
            case CONTROL_CONSTRUCT:
            case QUESTION_CONSTRUCT:
            case STATEMENT_CONSTRUCT:
            case CONDITION_CONSTRUCT:
            case SEQUENCE_CONSTRUCT:
                return  (ElementRefTyped<ControlConstruct>)element;
            default:
                return null;
        }
    }

}
