package no.nsd.qddt.domain.controlconstruct.pojo;

/**
 * @author Stig Norland
 */
public enum SequenceKind {
    NA("N/A","Not Applicable"),
//    QUESTIONNAIRE("Questionnare Sequence","Covers the content of a full questionnaire"),
    SECTION("Section Sequence","Covers the content of a section of a questionnaire section"),
    BATTERY("Battery Sequence","Covers content of a questionnaire battery"),
    UNIVERSE("Universe Sequence","Covers content for a specific universe or population"),
    LOOP("ForEach","For each Response do Sequence");

    SequenceKind(String name, String description){
        this.name = name;
        this.description = description;
    }

    private final String name;

    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static SequenceKind getEnum(String name) {
        if(name == null)
            throw new IllegalArgumentException();
        for(SequenceKind v : values())
            if(name.equalsIgnoreCase(v.getName())) return v;
        throw new IllegalArgumentException();
    }

}
