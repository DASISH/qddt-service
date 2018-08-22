package no.nsd.qddt.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.pojo.ControlConstruct;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.role.Authority;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.user.json.UserJsonEdit;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static no.nsd.qddt.utils.StringTool.SafeString;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */


@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "USER_ACCOUNT", uniqueConstraints = {@UniqueConstraint(columnNames = {"email" },name = "UNQ_USER_EMAIL")})
public class User {

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "username", updatable = false ,insertable = false)
    private String name;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    private boolean isEnabled;

    @Column(name = "updated")
    @Version()
    private Timestamp modified;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_authority",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @NotAudited
	  @OneToMany(mappedBy="modifiedBy")
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy="modifiedBy")
    private Set<Study> studies = new HashSet<>();

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy")
    private Set<Instrument> instrument = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy")
    private Set<ControlConstruct> controlConstructs = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy")
    private Set<Instruction> instructions = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy")
    private Set<QuestionItem> questions = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy")
    private Set<TopicGroup> topicGroups = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy")
    private Set<Concept> concepts = new HashSet<>();

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy="modifiedBy")
    private Set<ResponseDomain> responseDomains = new HashSet<>();


    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy="modifiedBy")
    private Set<Category> categories = new HashSet<>();


    public User() {
    }

    public User(String name, String email, String password) {
        setUsername(name);
        setEmail(email);
        setPassword(password);
    }

    public User(UserJsonEdit instance) {
        setId( instance.getId() );
        setUsername( instance.getName() );
        setEmail( instance.getEmail() );
        setEnabled( instance.isEnabled() );
        setAuthorities( new HashSet<>( Arrays.asList( instance.getAuthority()) ));
        setAgency( instance.getAgency() );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return SafeString(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return SafeString(username);
    }

    public void setName(String username) {
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
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

    public Set<Instrument> getInstrument() {
        return instrument;
    }

    public void setInstrument(Set<Instrument> instrument) {
        this.instrument = instrument;
    }

    public Set<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }

    public void setControlConstructs(Set<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
    }

    public Set<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Set<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Set<QuestionItem> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionItem> questions) {
        this.questions = questions;
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

    public Set<ResponseDomain> getResponseDomains() {
        return responseDomains;
    }

    public void setResponseDomains(Set<ResponseDomain> responseDomains) {
        this.responseDomains = responseDomains;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return (email != null ? !email.equals(user.email) : user.email != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " \"User \" { \"name\" : \"" + getUsername() + "@" + (agency!=null ? agency.getName(): "?") + "\" } ";
    }
}
