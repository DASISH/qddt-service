package no.nsd.qddt.domain.category;

/**
 * @author Stig Norland
 * @link Category
 */
public enum CategoryType {
    /*
    DateTime: single USER INPUT Category, input date and time   ,NOT_IMPLEMENTED:blankIsMissingValue |regExp
     */
    DATETIME,
    /*
    Text: single USER INPUT Category, input one line of text    ,NOT_IMPLEMENTED: blankIsMissingValue |maxLength |minLength |regExp
     */
    TEXT,
    /*
    Numeric: single USER INPUT Category, input is a number      ,NOT_IMPLEMENTED: blankIsMissingValue |format |scale |decimalPositions |interval
     */
    NUMERIC,
    /*
    Boolean, True or false. Can be represented by 1 and 0 correspondingly.
     */
    BOOLEAN,
    /*
    A Uniform Resource Identifier such as ftp, http or mailto, e.g., http://www.w3.org/TR/xmlschema-2.
     */
    URI,
    /*
    Code: single Category, input is CODE/VALUE                  ,NOT_IMPLEMENTED: blankIsMissingValue
     */
    CATEGORY,
    /*
    Missing values: CategoryList/CodeList that are used as missingvalues.
     */
    MISSING_GROUP,
    /*
    List: CategoryList/CodeList                                 ,NOT_IMPLEMENTED: xml:lang |isMaintainable |isSystemMissingValue
     */
    LIST,
    /*
    CategoryGroup/root -> ScaleDomain/ input is CODE/VALUE pairs,NOT_IMPLEMENTED: blankIsMissingValue
     */
    SCALE,
    /*
    ONLY for CategoryRoot -> a collection of different responsedomains
     */
    MIXED

}
