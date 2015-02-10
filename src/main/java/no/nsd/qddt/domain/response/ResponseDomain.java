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
 *
 * GridResponseDomain;      Designates the response domain and the cells using the specified response domain within a QuestionGrid.
 * QuestionGrid;            Structures the QuestionGrid as an NCube-like structure providing dimension information, labeling options, and response domains attached to one or more cells within the grid.
 * QuestionItem;            Structure a single Question which may contain one or more response domains (i.e., a list of valid category responses where if "Other" is indicated a text response can be used to specify the intent of "Other").
 * ResponseDomainInMixed;   A structure that provides both the response domain and information on how it should be attached, or related, to other specified response domains in the question.
 * ->   CategoryDomain;     A response domain capturing a category (without an attached code) response for a question item.
 *      CodeDomain;         A response domain capturing a coded response (where both codes and their related category value are displayed) for a question item.
 *      DateTimeDomain;     A response domain capturing a date or time response for a question item.
 *      DistributionDomain; A response domain capturing a distribution response for a question, may only be included in-line.
 *      GeographicDomain;   A response domain capturing a geographic coordinate reading as a response for a question item.
 *      GeographicLocationCodeDomain; A response domain capturing the name or code of a Geographic Location as a response for a question item, may only be included in-line.
 *      GeographicStructureCodeDomain;A response domain capturing a geographic structure code as a response for a question item, may only be included in-line.
 *      LocationDomain;     A response domain capturing a location response (mark on an image, recording, or object) for a question, may only be included in-line.
 *      NominalDomain;      A response domain capturing a nominal (check off) response for a question grid response, may only be included in-line.
 *      NumericDomain;      A response domain capturing a numeric response (the intent is to analyze the response as a number) for a question item.
 *      RankingDomain;      A response domain capturing a ranking response which supports a "ranking" of categories. Generally used within a QuestionGrid, may only be included in-line.
 *      ScaleDomain;        A response domain capturing a scale response which describes a 1..n dimensional scale of various display types for a question.
 *      TextDomain;         A response domain capturing a textual response.
 *
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
