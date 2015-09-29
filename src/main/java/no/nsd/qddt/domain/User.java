package no.nsd.qddt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.instrument.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import no.nsd.qddt.domain.response.Code;
import no.nsd.qddt.domain.response.ResponseDomain;
import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.security.Authority;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "user_account")
public class User {

    @Id
    @Column(name = "id")
    @Type(type="pg-uuid")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="user_authority",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Study> studies = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Instrument> instrument = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<InstrumentQuestion> instrumentQuestions = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Instruction> instructions = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<TopicGroup> topicGroups = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<ResponseDomain> responseDomains = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<ResponseDomainCode> responseDomainCodes = new HashSet<>();

    @OneToMany(mappedBy="createdBy", cascade = CascadeType.ALL)
    private Set<Code> codes = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;


    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Instrument> getInstrument() {
        return instrument;
    }

    public void setInstrument(Set<Instrument> instrument) {
        this.instrument = instrument;
    }

    public Set<InstrumentQuestion> getInstrumentQuestions() {
        return instrumentQuestions;
    }

    public void setInstrumentQuestions(Set<InstrumentQuestion> instrumentQuestions) {
        this.instrumentQuestions = instrumentQuestions;
    }

    public Set<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Set<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<TopicGroup> getTopicGroups() {
        return topicGroups;
    }

    public void setTopicGroups(Set<TopicGroup> topicGroups) {
        this.topicGroups = topicGroups;
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    public Set<ResponseDomain> getResponseDomains() {
        return responseDomains;
    }

    public void setResponseDomains(Set<ResponseDomain> responseDomains) {
        this.responseDomains = responseDomains;
    }

    public Set<ResponseDomainCode> getResponseDomainCodes() {
        return responseDomainCodes;
    }

    public void setResponseDomainCodes(Set<ResponseDomainCode> responseDomainCodes) {
        this.responseDomainCodes = responseDomainCodes;
    }

    public Set<Code> getCodes() {
        return codes;
    }

    public void setCodes(Set<Code> codes) {
        this.codes = codes;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (authorities != null ? !authorities.equals(user.authorities) : user.authorities != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (authorities != null ? authorities.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
