package no.nsd.qddt.domain.category;

/**
 * @author Stig Norland
 */
public enum CategoryType {
    /*
    Time: single Category
     */
    TIME,
    /*
    Date: single Category
     */
    DATE,
    /*
    DateTime: single Category
     */
    DATETIME,
    /*
    Text: single Category
     */
    TEXT,
    /*
    TextMulti: single Category
     */
    TEXT_MULTI,
    /*
    Numeric: single Category
     */
    NUMERIC,
    /*
    Code: single Category
     */
    CODE,
    /*
    List: single Category
     */
    LIST,
    /*
    Label: single Category, not for input
     */
    LABEL,
    /*
    CategoryGroup/root
     */
    RANGE,
    /*
    CategoryGroup/root
     */
    MULTIPLE_SINGLE,
    /*
    CategoryGroup/root
     */
    MULTIPLE_MULTIPLE,
    /*
    ONLY for CategoryRoot
     */
    MIXED

}
