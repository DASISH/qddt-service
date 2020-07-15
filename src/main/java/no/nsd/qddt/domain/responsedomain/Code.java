package no.nsd.qddt.domain.responsedomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.utils.StringTool;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 *
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@Embeddable
public class Code implements Comparable<Code> {

    @Column(name = "code_value")
    private String value;

    public Code() {
        value = "";
    }

    public Code(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Code code = (Code) o;

        return value != null ? value.equals( code.value ) : code.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"Code\", " +
            "\"value\":" + (value == null ? "null" : "\"" + value + "\"") + ", " +
            "}";
    }


    @Override
    public int compareTo(Code o) {
        try {
            Integer a = Integer.parseInt(value);
            Integer b = Integer.parseInt(o.getValue());
            return a.compareTo(b);
        } catch (NumberFormatException nfe) {
            return value.compareTo(o.getValue());
        }
    }

    @JsonIgnore
    public boolean isEmpty() {
        return StringTool.IsNullOrTrimEmpty( getValue() );
    }
}
