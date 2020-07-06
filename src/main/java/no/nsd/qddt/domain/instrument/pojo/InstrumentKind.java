package no.nsd.qddt.domain.instrument.pojo;

/**
 * @author Stig Norland
 */
public enum InstrumentKind {
    QUESTIONNAIRE("Questionnaire","Set of pre-determined questions presented to study participants."),
    QUESTIONNAIRE_STRUCTURED("Questionnaire Structured","Pre-determined questions, >2/3 which are closed-ended"),
    QUESTIONNAIRE_SEMISTRUCTURED("Questionnaire SemiStructured","Pre-determined questions, 1/3 ... 2/3 which are open-ended"),
    QUESTIONNAIRE_UNSTRUCTURED("Questionnaire Unstructured","Pre-determined questions, >2/3 are open-ended"),
    INTERVIEW_SCHEME_AND_THEMES("InterviewSchemeAndThemes","Themes / topics / questions used in an interview. Can vary between loosely defined themes to more exactly formulated questions"),
    DATA_COLLECTION_GUIDELINES("DataCollectionGuidelines","Guidelines and directions that define the content of the data capture"),
    DATACOLLECTIONGUIDELINES_OBSERVATIONGUIDE("DataCollectionGuidelines ObservationGuide","Guidelines regarding what will be observed. Depending on the study design, an observation guide can be more or less structured"),
    DATACOLLECTIONGUIDELINES_DISCUSSIONGUIDE("DataCollectionGuidelines DiscussionGuide","Guidelines for a group discussion. Depending on the study design, a discussion guide can be more or less structured"),
    DATACOLLECTIONGUIDELINES_SELFADMINISTEREDWRITINGSGUIDE("DataCollectionGuidelines SelfAdministeredWritingsGuide","Guidelines regarding the desired, or expected content of self-written personal accounts or narratives from potential participants"),
    DATACOLLECTIONGUIDELINES_SECONDARYDATACOLLECTIONGUIDE("DataCollectionGuidelines SecondaryDataCollectionGuide","Guidelines specifying what data are to be collected from previously existing sources originally created for other purposes"),
    PARTICIPANT_TASKS("ParticipantTasks","A description of tasks that participants are asked to carry out as a part of the data collection process"),
    TECHNICAL_INSTRUMENTS("TechnicalInstruments","Instruments used to collect objective data like measurements, images, etc."),
    PROGRAMMING_SCRIPT("ProgrammingScript","Programming script written in a data query language that is used to extract specific data, for instance from online social networks."),
    OTHER("Other","Use when the type of instrument is known, but not found in the list.");

    InstrumentKind(String name, String description){
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

    public static InstrumentKind getEnum(String name) {
        if(name == null)
            throw new IllegalArgumentException();
        for(InstrumentKind v : values())
            if(name.equalsIgnoreCase(v.getName())) return v;
        throw new IllegalArgumentException();
    }
}
