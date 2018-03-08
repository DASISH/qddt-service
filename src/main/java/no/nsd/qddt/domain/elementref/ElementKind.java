package no.nsd.qddt.domain.elementref;

/**
 * @author Stig Norland
 */
public enum ElementKind {
    SURVEY_PROGRAM("Survey"),
    STUDY("Study"),
    TOPIC_GROUP("Module"),
    CONCEPT("Concept"),
    QUESTION_ITEM("QuestionItem"),
    RESPONSEDOMAIN("ResponseDomain"),
    INSTRUMENT("Instrument"),
    PUBLICATION("Publication"),
    CONTROL_CONSTRUCT("ControlConstruct"),
    QUESTION_CONSTRUCT("QuestionConstruct"),
    STATEMENT_CONSTRUCT("Statement"),
    CONDITION_CONSTRUCT("Condition"),
    SEQUENCE_CONSTRUCT("Sequence");


    private final String description;

    ElementKind(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static ElementKind getEnum(String description) {
        if(description == null)
            throw new IllegalArgumentException();
        for(ElementKind v : values())
            if(description.equalsIgnoreCase(v.getDescription())) return v;
        throw new IllegalArgumentException();
    }
}
