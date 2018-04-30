package no.nsd.qddt.domain.topicgroup.json;

import no.nsd.qddt.domain.AbstractJsonEdit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.refclasses.StudyRef;
import no.nsd.qddt.domain.topicgroup.TopicGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class TopicGroupJson extends AbstractJsonEdit {

    private String abstractDescription;

    private List<ElementRef> topicQuestionItems;

    private Set<Author> authors = new HashSet<>();

    private List<OtherMaterial> otherMaterials = new ArrayList<>();

    private boolean isArchived;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private StudyRef studyRef;

    public TopicGroupJson(TopicGroup topicGroup) {
        super(topicGroup);
        if (topicGroup == null) return;
        setAbstractDescription(topicGroup.getAbstractDescription());
        setTopicQuestionItems(topicGroup.getTopicQuestionItems());
        setAuthors(topicGroup.getAuthors());
        setOtherMaterials(topicGroup.getOtherMaterials());
        setArchived(topicGroup.isArchived());
        setComments(topicGroup.getComments());
        studyRef = topicGroup.getStudyRef();
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    private void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    public List<ElementRef> getTopicQuestionItems() {
        return topicQuestionItems;
    }

    public void setTopicQuestionItems(List<ElementRef> topicQuestionItems) {
        this.topicQuestionItems = topicQuestionItems;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    private void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public List<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    private void setOtherMaterials(List<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials.stream()
            .map( om -> om.setOrgRef(this.getId()))
            .collect(Collectors.toList());
    }

    /**
     * @return the studyRef
     */
    public StudyRef getStudyRef() {
        return studyRef;
    }
    
    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroupJson)) return false;

        TopicGroupJson that = (TopicGroupJson) o;

        if (abstractDescription != null ? !abstractDescription.equals(that.abstractDescription) : that.abstractDescription != null)
            return false;
        if (topicQuestionItems != null ? !topicQuestionItems.equals(that.topicQuestionItems) : that.topicQuestionItems != null)
            return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (otherMaterials != null ? !otherMaterials.equals(that.otherMaterials) : that.otherMaterials != null)
            return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;
    }

    @Override
    public int hashCode() {
        int result = abstractDescription != null ? abstractDescription.hashCode() : 0;
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (otherMaterials != null ? otherMaterials.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "TopicGroupRevisionJson (abstractDescription=%s, topicQuestions=%s, authors=%s, otherMaterials=%s, comments=%s)",
                this.abstractDescription, this.topicQuestionItems, this.authors, this.otherMaterials, this.comments);
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
