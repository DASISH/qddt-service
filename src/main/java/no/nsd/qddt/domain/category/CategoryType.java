package no.nsd.qddt.domain.category;

/**
 * @author Stig Norland
 * @link
 */
public enum CategoryType {
    /*
    Time: single USER INPUT Category, input: time of day
     */
    TIME,
    /*
    Date: single USER INPUT Category, input: date without time specified
     */
    DATE,
    /*
    DateTime: single USER INPUT Category, input date and time
     */
    DATETIME,
    /*
    Text: single USER INPUT Category, input one line of text
     */
    TEXT,
    /*
    TextMulti: single USER INPUT Category, input several lines of text
     */
    TEXT_MULTI,
    /*
    Numeric: single USER INPUT Category, input is a number
     */
    NUMERIC,
    /*
    Code: single Category, input is CODE/VALUE (with a number representation)
     */
    CODE,
    /*
    List: single Category ??? CodeList maybe??? TODO if codelist remove
     */
    LIST,
    /*
    Label: single Category, NO INPUT, (usefull for scaledomains)
     */
    LABEL,
    /*
    Xml Element: NO INPUT, information that is needed for DDI, but not used by QDDT directly
     */
    XML_ELEMENT,
    /*
    CategoryGroup/root -> ScaleDomain/ input is CODE/VALUE pairs
     */
    RANGE,
    /*
    CategoryGroup/root -> a list of codes (codelist)
     */
    MULTIPLE_SINGLE,
    /*
    CategoryGroup/root -> a collection of  MULTIPLE_SINGLE/ RANGE
     */
    MULTIPLE_MULTIPLE,
    /*
    ONLY for CategoryRoot -> a collection of different responsdomains
     */
    MIXED

}
