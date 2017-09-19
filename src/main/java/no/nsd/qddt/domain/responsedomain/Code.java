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

    private String alignment;

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

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Code)) return false;

        Code code = (Code) o;

        if (codeValue != null ? !codeValue.equals(code.codeValue) : code.codeValue != null) return false;
        return alignment != null ? alignment.equals(code.alignment) : code.alignment == null;
    }

    @Override
    public int hashCode() {
        int result = codeValue != null ? codeValue.hashCode() : 0;
        result = 31 * result + (alignment != null ? alignment.hashCode() : 0);
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