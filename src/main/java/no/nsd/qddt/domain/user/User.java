package no.nsd.qddt.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.controlconstruct.ControlConstruct;
import no.nsd.qddt.domain.instruction.Instruction;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.ResponseDomain;
import no.nsd.qddt.domain.role.Authority;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static no.nsd.qddt.utils.StringTool.SafeString;

/**
 * @author Dag Østgulen Heradstveit
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "USER_ACCOUNT")
public class User {

    private UUID id;
    private String username;
    private String password;
    private String email;
    private Agency agency;
    private Set<Authority> authorities = new HashSet<>();
    private boolean isEnabled;

    
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();
    private Set<Study> studies = new HashSet<>();
    private Set<Comment> comments = new HashSet<>();
    private Set<Instrument> instrument = new HashSet<>();
    private Set<ControlConstruct> controlConstructs = new HashSet<>();
    private Set<Instruction> instructions = new HashSet<>();
    private Set<QuestionItem> questions = new HashSet<>();
    private Set<TopicGroup> topicGroups = new HashSet<>();
    private Set<OtherMaterial> otherMaterials = new HashSet<>();
    private Set<Concept> concepts = new HashSet<>();
    private Set<ResponseDomain> responseDomains = new HashSet<>();
    private Set<Category> categories = new HashSet<>();


    public User() {
    }

    public User(String name, String email, String password) {
        setUsername(name);
        setEmail(email);
        setPassword(password);
    }

    @Id
    @Type(type="pg-uuid")
    @Column(name = "id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }


    @Column(name = "username")
    public String getUsername() {
        return SafeString(username);
    }
    public void setUsername(String username) {
        this.username = username;
    }


    @JsonIgnore
    @Column(name = "password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Column(name = "email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "agency_id")
    public Agency getAgency() {
        return agency;
    }
    public void setAgency(Agency agency) {
        this.agency = agency;
    }


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="user_authority",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="authority_id"))
    public Set<Authority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Column(name = "is_enabled")
    public boolean isEnabled() {
        return isEnabled;
    }
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @NotAudited
    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<Comment> getComments() {
        return comments;
    }
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<SurveyProgram> getSurveyPrograms() {
        return surveyPrograms;
    }
    public void setSurveyPrograms(Set<SurveyProgram> surveyPrograms) {
        this.surveyPrograms = surveyPrograms;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<Study> getStudies() {
        return studies;
    }
    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<Instrument> getInstrument() {
        return instrument;
    }
    public void setInstrument(Set<Instrument> instrument) {
        this.instrument = instrument;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<ControlConstruct> getControlConstructs() {
        return controlConstructs;
    }
    public void setControlConstructs(Set<ControlConstruct> controlConstructs) {
        this.controlConstructs = controlConstructs;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<Instruction> getInstructions() {
        return instructions;
    }
    public void setInstructions(Set<Instruction> instructions) {
        this.instructions = instructions;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<QuestionItem> getQuestions() {
        return questions;
    }
    public void setQuestions(Set<QuestionItem> questions) {
        this.questions = questions;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<TopicGroup> getTopicGroups() {
        return topicGroups;
    }
    public void setTopicGroups(Set<TopicGroup> topicGroups) {
        this.topicGroups = topicGroups;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }
    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<Concept> getConcepts() {
        return concepts;
    }
    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<ResponseDomain> getResponseDomains() {
        return responseDomains;
    }
    public void setResponseDomains(Set<ResponseDomain> responseDomains) {
        this.responseDomains = responseDomains;
    }

    @JsonIgnore
    @OneToMany(mappedBy="modifiedBy", cascade = CascadeType.PERSIST)
    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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
        return "User{ " + username + "@" + agency.getName() + " }";
    }
}
