package no.nsd.qddt.domain.publication;

/**
 * @author Stig Norland
 */
public enum ElementKind {
    CONCEPT("Concept"),
    CONTROL_CONSTRUCT("Construct"),
    QUESTION_CONSTRUCT("Question Construct"),
    STATEMENT_CONSTRUCT("Statement"),
    SEQUENCE_CONSTRUCT("Sequence"),
    INSTRUMENT("Instrument"),
    QUESTION_ITEM("Question Item"),
    STUDY("Study"),
    SURVEY_PROGRAM("Survey Program"),
    TOPIC_GROUP("Questionnaire Module");


    private String description;
    ElementKind(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static ElementKind getEnum(String value) {
        if(value == null)
            throw new IllegalArgumentException();
        for(ElementKind v : values())
            if(value.equalsIgnoreCase(v.getDescription())) return v;
        throw new IllegalArgumentException();
    }
}
