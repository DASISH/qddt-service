package no.nsd.qddt.domain.embedded;

import javax.persistence.Embeddable;

/**
 * Indicates the minimum and maximum number of occurrences of a response within the given parameters.
 *
 * @author Stig Norland
 */
@Embeddable
public class ResponseCardinality {

    private String minimum;

    private String maximum;

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public ResponseCardinality(){
        this("1","1");
    }

    public ResponseCardinality(String minimum, String maximum) {
        this.setMinimum(minimum);
        this.setMaximum(maximum);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseCardinality)) return false;

        ResponseCardinality that = (ResponseCardinality) o;

        if (minimum != that.minimum) return false;
        return maximum == that.maximum;

    }

    @Override
    public int hashCode() {
        int result = minimum != null ? minimum.hashCode() : 0;
        result = 31 * result + (maximum != null ? maximum.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "valid response{ minimum=" + minimum + ", maximum=" + maximum +'}';
    }
}
