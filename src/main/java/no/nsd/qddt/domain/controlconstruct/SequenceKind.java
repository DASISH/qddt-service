package no.nsd.qddt.domain.controlconstruct;

/**
 * @author Stig Norland
 */
public enum SequenceKind {
    NA("Not Applicable"),
    QUESTIONNAIRE("A sequence that covers the content of a full questionnaire"),
    SECTION("A sequence that covers the content of a section of a questionnaire section"),
    BATTERY("A sequence that covers content of a questionnaire battery"),
    UNIVERSE("A sequence that covers content for a specific universe or population");

    private SequenceKind(String value){
        this.value = value;
    }

    private final String value;

    public String getValue(){return value;}
}
