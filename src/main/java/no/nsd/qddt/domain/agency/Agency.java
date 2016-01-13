package no.nsd.qddt.domain.agency;


import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.question.Question;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


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
public class Agency extends AbstractEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Study>  studies = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Instrument> instruments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Instruction> instructions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<TopicGroup> topicGroups = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<ResponseDomain> responses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="agency", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    @Column(name = "name", length = 50)
    private String name;

    public Agency(){

    }

    public String getName() {
        return name;
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

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agency)) return false;
        if (!super.equals(o)) return false;

        Agency agency = (Agency) o;
//        if (surveyPrograms != null ? !surveyPrograms.equals(agency.surveyPrograms) : agency.surveyPrograms != null)
//            return false;
//        if (studies != null ? !studies.equals(agency.studies) : agency.studies != null) return false;
//        if (instruments != null ? !instruments.equals(agency.instruments) : agency.instruments != null) return false;
//        if (instructions != null ? !instructions.equals(agency.instructions) : agency.instructions != null)
//            return false;
//        if (topicGroups != null ? !topicGroups.equals(agency.topicGroups) : agency.topicGroups != null) return false;
//        if (concepts != null ? !concepts.equals(agency.concepts) : agency.concepts != null) return false;
//        if (questions != null ? !questions.equals(agency.questions) : agency.questions != null) return false;
//        if (responses != null ? !responses.equals(agency.responses) : agency.responses != null) return false;
//        if (categories != null ? !categories.equals(agency.categories) : agency.categories != null) return false;
        return !(name != null ? !name.equals(agency.name) : agency.name != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
//        result = 31 * result + (surveyPrograms != null ? surveyPrograms.hashCode() : 0);
//        result = 31 * result + (studies != null ? studies.hashCode() : 0);
//        result = 31 * result + (instruments != null ? instruments.hashCode() : 0);
//        result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
//        result = 31 * result + (topicGroups != null ? topicGroups.hashCode() : 0);
//        result = 31 * result + (concepts != null ? concepts.hashCode() : 0);
//        result = 31 * result + (questions != null ? questions.hashCode() : 0);
//        result = 31 * result + (responses != null ? responses.hashCode() : 0);
//        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "name='" + name + '\'' +
                "} "; //+ super.toString();
    }
}
