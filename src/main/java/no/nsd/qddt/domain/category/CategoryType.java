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
    Code: single Category, input is CODE/VALUE                  ,NOT_IMPLEMENTED: blankIsMissingValue
     */
    CODE,

    /*
    Label: single Category, NO INPUT, REMOVED -> is basically a CODE and its value is where on a scale it is.
     */

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
    RANGE,
    /*
    ONLY for CategoryRoot -> a collection of different responsedomains
     */
    MIXED

}
