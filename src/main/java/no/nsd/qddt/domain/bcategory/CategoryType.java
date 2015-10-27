package no.nsd.qddt.domain.bcategory;

/**
 * @author Stig Norland
 */
public enum CategoryType {
    /*
    single Category
     */
    TIME,
    /*
    single Category
     */
    DATE,
    /*
    single Category
     */
    DATETIME,
    /*
    single Category
     */
    TEXT,
    /*
    single Category
     */
    TEXT_MULTI,
    /*
    single Category
     */
    NUMERIC,
    /*
    single Category
     */
    LIST,
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
