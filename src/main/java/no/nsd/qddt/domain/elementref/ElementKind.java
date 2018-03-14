package no.nsd.qddt.domain.elementref;

/**
 * @author Stig Norland
 */
public enum ElementKind {
    SURVEY_PROGRAM("Survey","SurveyProgram"),
    STUDY("Study","Study"),
    TOPIC_GROUP("Module","TopicGroup"),
    CONCEPT("Concept","Concept"),
    QUESTION_ITEM("QuestionItem","QuestionItem"),
    RESPONSEDOMAIN("ResponseDomain","ResponseDomain"),
    INSTRUMENT("Instrument","Instrument"),
    PUBLICATION("Publication","Publication"),
    CONTROL_CONSTRUCT("ControlConstruct","ControlConstruct"),
    QUESTION_CONSTRUCT("QuestionConstruct","ControlConstruct"),
    STATEMENT_CONSTRUCT("Statement","ControlConstruct"),
    CONDITION_CONSTRUCT("Condition","ControlConstruct"),
    SEQUENCE_CONSTRUCT("Sequence","ControlConstruct");


    private final String description;
    private final String className;

    ElementKind(String description, String className) {
        this.description = description;
        this.className = className;
    }

    public String getDescription() {
        return this.description;
    }

    public String getClassName() {
        return className;
    }

    public static ElementKind getEnum(String className) {
        if(className == null)
            throw new IllegalArgumentException();
        for(ElementKind v : values())
            if(className.equalsIgnoreCase(v.getClassName())) return v;
        throw new IllegalArgumentException();
    }
}
