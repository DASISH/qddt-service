package no.nsd.qddt.domain.category;

/**
 * @author Stig Norland
 */
public enum CategoryRelationCodeType {
    /**
     * A relationship of less than, or greater than, cannot be established among the included categories. This type of relationship is also called categorical or discrete.
     */
    Nominal,
    /**
     * The categories in the domain have a rank order.
     */
    Ordinal,
    /**
     * The categories in the domain are in rank order and have a consistent interval between each category so that differences between arbitrary pairs of measurements can be meaningfully compared.
     */
    Interval,
    /**
     * The categories have all the features of interval measurement and also have meaningful ratios between arbitrary pairs of numbers.
     */
    Ratio,
    /**
     * May be used to identify both interval and ratio classification levels, when more precise information is not available.
     */
    Continuous

}
