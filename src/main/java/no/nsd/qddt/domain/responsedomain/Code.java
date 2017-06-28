package no.nsd.qddt.domain.responsedomain;

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
    private String codeValue;


    public Code() {
        codeValue = "";
    }


    public Code(String codeValue){
        this.setCodeValue(codeValue);
    }


    public String getCodeValue() {
        return codeValue;
    }

    private void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Code)) return false;
        if (!super.equals(o)) return false;

        Code code = (Code) o;

        return codeValue != null ? codeValue.equals(code.codeValue) : code.codeValue == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (codeValue != null ? codeValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Code { %s }" ,getCodeValue());
    }

    @Override
    public int compareTo(Code o) {
        try {
            Integer a = Integer.parseInt(codeValue);
            Integer b = Integer.parseInt(o.getCodeValue());
            return a.compareTo(b);
        } catch (NumberFormatException nfe) {
            return codeValue.compareTo(o.getCodeValue());
        }
    }
}