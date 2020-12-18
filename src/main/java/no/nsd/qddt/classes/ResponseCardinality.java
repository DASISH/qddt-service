package no.nsd.qddt.classes;


import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * Indicates the minimum and maximum number of occurrences of a response within the given parameters.
 *
 * @author Stig Norland
 */
@Embeddable
public class ResponseCardinality {

    @NotBlank
    private Integer minimum;

    @NotBlank
    private Integer maximum;

    @NotBlank
    private Integer stepUnit;

    public ResponseCardinality() {
        this(0,1,1);
    }

    public ResponseCardinality(Integer minimum, Integer maximum, Integer stepUnit) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepUnit = stepUnit;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Integer getStepUnit() {
        return stepUnit;
    }

    public void setStepUnit(Integer stepUnit) {
        this.stepUnit = stepUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseCardinality that = (ResponseCardinality) o;

        if (!minimum.equals( that.minimum )) return false;
        if (!maximum.equals( that.maximum )) return false;
        return stepUnit.equals( that.stepUnit );
    }

    @Override
    public int hashCode() {
        int result = minimum.hashCode();
        result = 31 * result + maximum.hashCode();
        result = 31 * result + stepUnit.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{ minimum=" + minimum + ", maximum=" + maximum + ", step=" + stepUnit +'}';
    }

    @Transient
    public boolean isValid() {
        return  (minimum != null && maximum != null &&  minimum <= maximum && ( stepUnit >=-10 && stepUnit <= 10));
    }
}
