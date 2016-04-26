package no.nsd.qddt.domain.author;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.study.Study;
import no.nsd.qddt.domain.surveyprogram.SurveyProgram;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stig Norland
 */

@Entity
@Table(name = "AUTHOR")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Author extends AbstractEntity {

    @Column(name = "name", length = 70,nullable = false)
    private String name;

    private String email;

    @Column(name = "about", length = 500)
    private String about;

    private URL homepage;

    private URL picture;

    private String authorsAffiliation;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "AUTHOR_SURVEY",
            joinColumns = {@JoinColumn(name ="author_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "survey_id", nullable = false,referencedColumnName = "id")})
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "AUTHOR_STUDY",
            joinColumns = {@JoinColumn(name ="author_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "study_id", nullable = false,referencedColumnName = "id")})
    private Set<Study> studies = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "AUTHOR_TOPIC",
            joinColumns = {@JoinColumn(name ="author_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "topic_id", nullable = false,referencedColumnName = "id")})
    private Set<TopicGroup> topicGroups = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "AUTHOR_CONCEPTS",
            joinColumns = {@JoinColumn(name ="author_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "concept_id", nullable = false,referencedColumnName = "id")})
    private Set<Concept> concepts = new HashSet<>();



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public URL getHomepage() {
        return homepage;
    }

    public void setHomepage(URL homepage) {
        this.homepage = homepage;
    }

    public URL getPicture() {
        return picture;
    }

    public void setPicture(URL picture) {
        this.picture = picture;
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

    public void addSurvey(SurveyProgram surveyProgram){
        if (!surveyPrograms.contains(surveyProgram)) {
            surveyPrograms.add(surveyProgram);
        }
    }

    public void addStudy(Study study){
        if (!studies.contains(study)) {
            this.studies.add(study);
        }
    }

    public void addTopic(TopicGroup topicGroup){
        if (!topicGroups.contains(topicGroup)) {
            this.topicGroups.add(topicGroup);
        }
    }

    public void addConcept(Concept concept){
        if (!concepts.contains(concept)) {
            this.concepts.add(concept);
        }
    }

    public void removeSurvey(SurveyProgram surveyProgram){
        if (surveyPrograms.contains(surveyProgram))
            surveyPrograms.remove(surveyProgram);
    }

    public void removeStudy(Study study){
        if (studies.contains(study)) {
            this.studies.remove(study);
        }
    }

    public void removeTopic(TopicGroup topicGroup){
        if (topicGroups.contains(topicGroup)) {
            this.topicGroups.remove(topicGroup);
        }
    }

    public void removeConcept(Concept concept){
        if (concepts.contains(concept)) {
            this.concepts.remove(concept);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        if (!super.equals(o)) return false;

        Author author = (Author) o;

        if (name != null ? !name.equals(author.name) : author.name != null) return false;
        if (email != null ? !email.equals(author.email) : author.email != null) return false;
        if (about != null ? !about.equals(author.about) : author.about != null) return false;
        if (homepage != null ? !homepage.equals(author.homepage) : author.homepage != null) return false;
        return picture != null ? picture.equals(author.picture) : author.picture == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (about != null ? about.hashCode() : 0);
        result = 31 * result + (homepage != null ? homepage.hashCode() : 0);
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", about='" + about + '\'' +
                ", homepage=" + homepage +
                ", picture=" + picture +
                "} " + super.toString();
    }

    public String getAuthorsAffiliation() {
        return authorsAffiliation;
    }

    public void setAuthorsAffiliation(String authorsAffiliation) {
        this.authorsAffiliation = authorsAffiliation;
    }
}
