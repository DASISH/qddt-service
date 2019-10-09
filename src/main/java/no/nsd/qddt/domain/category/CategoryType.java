package no.nsd.qddt.domain.category;

import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;

/**
 * @author Stig Norland
 * @see Category
 */
public enum CategoryType {
    /**
     *  single USER INPUT Category,
     */
    DATETIME("DateTime","Single USER INPUT Category, input date and time","NOT_IMPLEMENTED:blankIsMissingValue |regExp"),
    /*
        single USER INPUT Category, input one line of text    ,NOT_IMPLEMENTED: blankIsMissingValue |maxLength |minLength |regExp
     */
    TEXT("Text","Single USER INPUT Category, input one line of text",""),
    /*
        single USER INPUT Category, input is a number      ,NOT_IMPLEMENTED: blankIsMissingValue |format |scale |decimalPositions |interval
     */
    NUMERIC("Numeric","Single USER INPUT Category, input is a number",""),
    /*
        True or false. Can be represented by 1 and 0 correspondingly.
     */
    BOOLEAN("Boolean","True or false. Can be represented by 1 and 0 correspondingly",""),
    /*
        A Uniform Resource Identifier such as ftp, http or mailto, e.g., http://www.w3.org/TR/xmlschema-2.
     */
    URI("Uniform Resource Identifier","A Uniform Resource Identifier such as ftp, http or mailto",""),
    /*
        Code: single Category, input is CODE/VALUE                  ,
     */
    CATEGORY("Code","Single Category, input is CODE/VALUE","NOT_IMPLEMENTED: blankIsMissingValue"),
    /*
        Missing values: CategoryList/CodeList that are used as missingvalues.
     */
    MISSING_GROUP("Missing values","CategoryList/CodeList that are used as missingvalues",""),
    /*
        List: CategoryList/CodeList                                 ,NOT_IMPLEMENTED: xml:lang |isMaintainable |isSystemMissingValue
     */
    LIST("CodeList","CategoryList/CodeList","NOT_IMPLEMENTED: xml:lang |isMaintainable |isSystemMissingValue"),
    /*
        CategoryGroup/root -> ScaleDomain/ input is CODE/VALUE pairs,NOT_IMPLEMENTED: blankIsMissingValue
     */
    SCALE("ScaleDomain","CategoryGroup/root -> ScaleDomain/ input is CODE/VALUE pairs",""),
    /*
        ONLY for CategoryRoot -> a collection of different responsedomains
     */
    MIXED("Mixed Mananged representation","ONLY for CategoryRoot -> a collection of different responsedomains","");

    CategoryType(String name, String description,String ddiComment){
        this.name = name;
        this.description = description;
        this.ddiComment = ddiComment;
    }

    private final String name;

    private final String description;

    private final String ddiComment;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDdiComment() {
        return ddiComment;
    }

    public static CategoryType getEnum(String name) {
        if(IsNullOrTrimEmpty(name))
            return null;
        for(CategoryType v : values())
            if(name.equalsIgnoreCase(v.toString()) || name.equalsIgnoreCase(v.getName()) ) return v;
        throw new IllegalArgumentException("Enum value not valid " + name);
    }

}
