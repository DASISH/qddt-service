package no.nsd.qddt.domain.responsedomain;

/**
 * A ResponseKind define what kind of ResponseDomain (answer) this is,
 * this will also define the way the Question is formatted.
 *
 * This Class would be a good candidate to change into a ENUM, as every entry
 * will have to be mapped to specific behaviour in GUI and intruments/surveys.
 *<dl>
 *      <dt>Category</dt><dd>A category (without an attached category) response for a question item.</dd>
 *      <dt>Code</dt><dd>A coded response (where both codes and their related category value are displayed) for a question item.</dd>
 *      <dt>Numeric</dt><dd>A numeric response (the intent is to analyze the response as a number) for a question item.</dd>
 *      <dt>Scale</dt><dd>A scale response which describes a 1..n dimensional scale of various display types for a question.</dd>
 *      <dt>Text</dt><dd>A textual response.</dd>
 *      <dt>Mixed</dt><dd>A response with two or more different subtypes.</dd>
 *
 * </dl>

 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */


public enum ResponseKind {
//    Category,
//    Code,
//    Numeric,
    List,
    Scale,
//    Text,
    Mixed
}


