package no.nsd.qddt.domain.response;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.Agency;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * CodeList A special form of maintainable that allows a single codelist to be maintained outside of a CodeListScheme.
 *<ul>
 * <li>QuestionGrid;      Structures the QuestionGrid as an NCube-like structure providing dimension information, labeling options, and response domains attached to one or more cells within the grid.</li>
 * <li>QuestionItem;      Structure a single Question which may contain one or more response domains (i.e., a list of valid category responses where if "Other" is indicated a text response can be used to specify the intent of "Other").</li>
 * <li>ResponseInMixed;   A structure that provides both the response domain and information on how it should be attached, or related, to other specified response domains in the question.</li>
 * <li>->   Category;     A category (without an attached code) response for a question item.</li>
 * <li>                   Code.Category = Code.codeValue;</li>
 * <li>     Code;         A coded response (where both codes and their related category value are displayed) for a question item.</li>
 * <li>                   Code.Category = "A_NAME", Code.CodeValue = "A_VALUE"</li>
 * <li>     Numeric;      A numeric response (the intent is to analyze the response as a number) for a question item.</li>
 * <li>                   Code = NULL; (no code is needed)</li>
 * <li>     Scale;        A scale response which describes a 1..n dimensional scale of various display types for a question.</li>
 * <li>                   Code.CodeValue = valid values 1..n + control codes (N/A, Refuses, can't answer, don't know etc)</li>
 * <li>     Text;         A textual response.</li>
 * <li>                   Code = NULL; (no code is needed)</li>
 * <li>These to be implemented later ->
 *      -DateTime;    A date or time response for a question item.
 *      -Distribution;A distribution response for a question, may only be included in-line.
 *      -Geographic;  A geographic coordinate reading as a response for a question item.
 *      -GeographicLocationCode; A response domain capturing the name or code of a Geographic Location as a response for a question item, may only be included in-line.
 *      -GeographicStructureCode;A geographic structure code as a response for a question item, may only be included in-line.
 *      -Location;    A location response (mark on an image, recording, or object) for a question, may only be included in-line.
 *      -Nominal;     A nominal (check off) response for a question grid response, may only be included in-line.
 *      -Ranking;     A ranking response which supports a "ranking" of categories. Generally used within a QuestionGrid, may only be included in-line.
 *</li></ul>
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "responseDomain")
public class ResponseDomain extends AbstractEntityAudit implements Serializable {

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "response_kind_id")
    private ResponseKind responseKind;

    @OneToMany(mappedBy="responseDomain", cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

    public Agency getAgency() {return agency;}

    public void setAgency(Agency agency) {this.agency = agency;}

    public ResponseKind getResponseKind() {
        return responseKind;
    }

    public void setResponseKind(ResponseKind responseKind) {
        this.responseKind = responseKind;
    }

    public Set<ResponseDomainCode> getResponseDomainCodes() {
        return responseDomainCodes;
    }

    public void setResponseDomainCodes(Set<ResponseDomainCode> responseDomainCodes) {
        this.responseDomainCodes = responseDomainCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ResponseDomain that = (ResponseDomain) o;

        if (agency != null ? !agency.equals(that.agency) : that.agency != null) return false;
        if (responseKind != null ? !responseKind.equals(that.responseKind) : that.responseKind != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (agency != null ? agency.hashCode() : 0);
        result = 31 * result + (responseKind != null ? responseKind.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResponseDomain{" +
                "agency=" + agency +
                ", responseKind=" + responseKind +
                super.toString() +
                '}';
    }
}
