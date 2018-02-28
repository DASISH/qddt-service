package no.nsd.qddt.domain.author;

import com.fasterxml.jackson.annotation.JsonBackReference;
import no.nsd.qddt.domain.AbstractEntity;
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


    @JsonBackReference(value = "surveyRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors",cascade = CascadeType.ALL)
    private Set<SurveyProgram> surveyPrograms = new HashSet<>();

    @JsonBackReference(value = "studyRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors",cascade = CascadeType.ALL)
    private Set<Study> studies = new HashSet<>();

    @JsonBackReference(value = "topicRef")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors",cascade = CascadeType.ALL)
    private final Set<TopicGroup> topicGroups = new HashSet<>();

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
        return "{\"_class\":\"Author\", " +
                "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
                "\"email\":" + (email == null ? "null" : "\"" + email + "\"") + ", " +
                "\"about\":" + (about == null ? "null" : "\"" + about + "\"") + ", " +
                "\"homepage\":" + (homepage == null ? "null" : homepage) + ", " +
                "\"picture\":" + (picture == null ? "null" : picture) + ", " +
                "\"authorsAffiliation\":" + (authorsAffiliation == null ? "null" : "\"" + authorsAffiliation + "\"") +
                "}";
    }

    public String getAuthorsAffiliation() {
        return authorsAffiliation;
    }

    public void setAuthorsAffiliation(String authorsAffiliation) {
        this.authorsAffiliation = authorsAffiliation;
    }

}
