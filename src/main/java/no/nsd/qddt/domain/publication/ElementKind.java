package no.nsd.qddt.domain.publication;

/**
 * @author Stig Norland
 */
public enum ElementKind {
    CONCEPT("Concept"),
    CONTROL_CONSTRUCT("Construct"),
    QUESTION_CONSTRUCT("QuestionConstruct"),
    STATEMENT_CONSTRUCT("Statement"),
    SEQUENCE_CONSTRUCT("Sequence"),
    CONDITION_CONSTRUCT("Condition"),
    INSTRUMENT("Instrument"),
    QUESTION_ITEM("QuestionItem"),
    STUDY("Study"),
    SURVEY_PROGRAM("Survey"),
    TOPIC_GROUP("Module");


    private String description;
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
