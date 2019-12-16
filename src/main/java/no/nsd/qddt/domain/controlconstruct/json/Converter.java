package no.nsd.qddt.domain.controlconstruct.json;

import no.nsd.qddt.domain.controlconstruct.pojo.*;

/**
 * @author Stig Norland
 */
public  class Converter {


    public static <S extends ConstructJson> S mapConstruct(ControlConstruct construct){
        switch (construct.getClassKind()) {
            case "QUESTION_CONSTRUCT":
                return (S)new ConstructQuestionJson((QuestionConstruct) construct);
            case "STATEMENT_CONSTRUCT":
                return (S)new ConstructStatementJson((StatementItem) construct);
            case "CONDITION_CONSTRUCT":
                return (S)new ConstructConditionJson((ConditionConstruct) construct);
            case "SEQUENCE_CONSTRUCT":
                return (S)new ConstructSequenceJson((Sequence) construct);
            default:
                return (S)new ConstructJson(construct);
        }
    }

    public static <S extends ConstructJsonView> S mapConstructView(ControlConstruct construct){
        switch (construct.getClassKind()) {
            case "QUESTION_CONSTRUCT":
                return (S)new ConstructQuestionJsonView((QuestionConstruct) construct);
            case "STATEMENT_CONSTRUCT":
                return (S)new ConstructStatementJsonView((StatementItem) construct);
            case "CONDITION_CONSTRUCT":
                return (S)new ConstructConditionJsonView((ConditionConstruct) construct);
            case "SEQUENCE_CONSTRUCT":
                return (S)new ConstructSequenceJsonView((Sequence) construct);
            default:
                return (S)new ConstructJsonView(construct);
        }
    }

}
