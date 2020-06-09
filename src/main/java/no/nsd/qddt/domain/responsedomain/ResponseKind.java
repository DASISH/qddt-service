package no.nsd.qddt.domain.responsedomain;

/**
 * A ResponseKind define what kind of ResponseDomain (answer) this is,
 * this will also define the way the Question is formatted.
 *
 * This Class would be a good candidate to change into a ENUM, as every entry
 * will have to be mapped to specific behaviour in GUI and intruments/surveys.
 *<dl>
 *      <dt>DATETIME</dt><dd>A datetime response</dd>
 *      <dt>TEXT</dt><dd>A textual response.</dd>
 *      <dt>NUMERIC</dt><dd>A numeric response (the intent is to analyze the response as a number) for a question item.</dd>
 *      <dt>LIST</dt><dd>A coded response (where both codes and their related category value are displayed) for a question item.</dd>
 *      <dt>SCALE</dt><dd>A scale response which describes a 1..n dimensional scale of various display types for a question.</dd>
 *      <dt>MIXED</dt><dd>A response with two or more different subtypes.</dd>
 * </dl>

 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */


public enum ResponseKind {
    DATETIME("DateTimeDomain","ManagedDateTimeRepresentation"),
    TEXT("TextDomain","ManagedTextRepresentation"),
    NUMERIC("NumericDomain","ManagedNumericRepresentation"),
    LIST("CodeDomain","Codelist"),
    SCALE("ScaleDomain","ManagedScaleRepresentation"),
    MIXED("ResponseDomainInMixed","");
    ResponseKind(String ddiName, String ddiRepresentation){
        this.name = ddiName;
        this.ddiRepresentation = ddiRepresentation;
    }

    private final String name;

    private final String ddiRepresentation;

    public String getDDIName() {
        return name;
    }

    public String getDdiRepresentation() {
        return ddiRepresentation;
    }


}

//"MissingCodeRepresentation",
//"MissingTextRepresentation",




