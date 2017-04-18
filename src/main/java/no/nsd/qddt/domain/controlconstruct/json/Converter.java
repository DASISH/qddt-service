package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.ControlConstruct;

/**
 * @author Stig Norland
 */
public  class Converter {


    public static ConstructJson mapConstruct(ControlConstruct construct){
        switch (construct.getControlConstructKind()) {
            case QUESTION_CONSTRUCT:
                return new ConstructQuestionJson(construct);
            case STATEMENT_CONSTRUCT:
                return new ConstructStatementJson(construct);
            case CONDITION_CONSTRUCT:
                return new ConstructConditionJson(construct);
            case SEQUENCE_CONSTRUCT:
                return new ConstructSequenceJson(construct);
            default:
                return new ConstructJson(construct);
        }
    }


}
