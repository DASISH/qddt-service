package no.nsd.qddt.classes.elementref;

/**
 * @author Stig Norland
 */
public enum ElementKind {
    SURVEY_PROGRAM      ("Survey","SurveyProgram",""),
    STUDY               ("Study","Study",""),
    TOPIC_GROUP         ("Module","TopicGroup","c"),
    CONCEPT             ("Concept","Concept","c"),
    QUESTION_ITEM       ("Question Item","QuestionItem", "d"),
    RESPONSEDOMAIN      ("Response Domain","ResponseDomain","r"),
    CATEGORY            ("Category","Category" ,"l"),
    INSTRUMENT          ("Instrument","Instrument", "c"),
    PUBLICATION         ("Publication","Publication", "c"),
    CONTROL_CONSTRUCT   ("Control Construct","ControlConstruct", "d"),
    QUESTION_CONSTRUCT  ("Question Construct","QuestionConstruct", "d"),
    STATEMENT_CONSTRUCT ("Statement","StatementItem", "d"),
    CONDITION_CONSTRUCT ("Condition","ConditionConstruct", "d"),
    SEQUENCE_CONSTRUCT  ("Sequence","Sequence" ,"c"),
    INSTRUCTION         ("Instruction","Instruction", "d"),
    UNIVERSE            ("Universe","Universe", "d"),
    COMMENT             ("Comment","Comment", "c");


    private final String description;
    private final String className;
    private final String ddiPreFix;

    ElementKind(String description, String className, String ddiPreFix) {
        this.description = description;
        this.className = className;
        this.ddiPreFix = ddiPreFix;
    }


    public String getDescription() {
        return this.description;
    }

    public String getClassName() {
        return className;
    }

    public String getDdiPreFix() {
        return ddiPreFix;
    }

    public static ElementKind getEnum(String className) {
        if(className == null)
            throw new IllegalArgumentException("className not spesified.");
        for(ElementKind v : values())
            if(className.equalsIgnoreCase(v.getClassName())) return v;

        return ElementKind.valueOf( className );

//        throw new IllegalArgumentException("className not found. [" + className + "]");
    }
}
