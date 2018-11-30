package no.nsd.qddt.domain.agency;


import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.publicationstatus.PublicationStatus;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static no.nsd.qddt.utils.StringTool.SafeString;

/**
 * The agency expressed as filed with the DDI Agency ID Registry with optional additional sub-agency extensions.
 * The length restriction of the complete string is done with the means of minLength and maxLength.
 * The regular expression engine of XML Schema has no lookahead possibility.
 *
 * We'll have a relationship with surveys and groups
 *
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "AGENCY")
public class Agency implements Comparable<Agency>{


    @Id
    @Type(type="pg-uuid")
    @GeneratedValue(generator ="UUID")
    @GenericGenerator(name ="UUID", strategy ="org.hibernate.id.UUIDGenerator")
    @Column(name ="id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "updated", nullable = false)
    @Version
    private Timestamp modified;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<User> users = new HashSet<>();


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<Study>  studies = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<Instrument> instruments = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<Instruction> instructions = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<TopicGroup> topicGroups = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<Concept> concepts = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<QuestionItem> questions = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<ResponseDomain> responses = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<Category> categories = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency")
    @NotAudited
    private Set<PublicationStatus> statuses = new HashSet<>();

    public Agency(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public String getName() {
        return SafeString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SurveyProgram> getSurveyPrograms() {
        return surveyPrograms;
    }

    public void setSurveyPrograms(Set<SurveyProgram> surveyPrograms) {
        this.surveyPrograms = surveyPrograms;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

    public Set<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Set<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Set<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(Set<TopicGroup> topicGroups) {
        this.topicGroups = topicGroups;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<QuestionItem> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionItem> questions) {
        this.questions = questions;
    }

    public Set<ResponseDomain> getResponses() {
        return responses;
    }

    public void setResponses(Set<ResponseDomain> responses) {
        this.responses = responses;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<PublicationStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<PublicationStatus> statuses) {
        this.statuses = statuses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agency)) return false;
        if (!super.equals(o)) return false;

        Agency agency = (Agency) o;
        return !(name != null ? !name.equals(agency.name) : agency.name != null);

    }



    @Override
    public int hashCode() {
        return  31 * super.hashCode() + (name != null ? name.hashCode() : 0);
    }

    @Override
    public String toString() {
        return String.format(
                "Agency (name=%s)", this.name);
    }

    public int compareTo(Agency o) {
        int i = this.getName().compareTo(o.getName());
        if (i!=0) return i;

        return this.getId().compareTo(o.getId());
    }
}
