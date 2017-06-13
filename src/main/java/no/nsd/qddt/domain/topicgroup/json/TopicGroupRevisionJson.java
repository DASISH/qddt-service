package no.nsd.qddt.domain.topicgroup.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.json.ConceptJsonView;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicQuestions;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItemJson;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class TopicGroupRevisionJson extends BaseJsonEdit {

    private String abstractDescription;

    private Set<TopicGroupQuestionItemJson> topicGroupQuestions;

    private Set<ConceptJsonView> concepts = new HashSet<>();

    private Set<Author> authors = new HashSet<>();

    private Set<OtherMaterial> otherMaterials = new HashSet<>();

    private Set<CommentJsonEdit> comments = new HashSet<>();

    public TopicGroupRevisionJson(TopicGroup topicGroup) {
        super(topicGroup);
        if (topicGroup == null) return;
        setAbstractDescription(topicGroup.getAbstractDescription());
        setTopicGroupQuestions( topicGroup.getTopicQuestionItems().stream().map(i->new TopicGroupQuestionItemJson(i)).collect(Collectors.toSet()));
        setAuthors(topicGroup.getAuthors());
        setOtherMaterials(topicGroup.getOtherMaterials());
        setComments(topicGroup.getComments().stream().map(F-> new CommentJsonEdit(F)).collect(Collectors.toSet()));
        setConcepts(topicGroup.getConcepts().stream().map(i->new ConceptJsonView(i)).collect(Collectors.toSet()));
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    public Set<TopicGroupQuestionItemJson> getTopicGroupQuestions() {
        return topicGroupQuestions;
    }

    public void setTopicGroupQuestions(Set<TopicGroupQuestionItemJson> topicGroupQuestions) {
        this.topicGroupQuestions = topicGroupQuestions;
    }

    public Set<ConceptJsonView> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<ConceptJsonView> concepts) {
        this.concepts = concepts;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<OtherMaterial> getOtherMaterials() {
        return otherMaterials;
    }

    public void setOtherMaterials(Set<OtherMaterial> otherMaterials) {
        this.otherMaterials = otherMaterials;
    }

    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    public void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicGroupRevisionJson)) return false;

        TopicGroupRevisionJson that = (TopicGroupRevisionJson) o;

        if (abstractDescription != null ? !abstractDescription.equals(that.abstractDescription) : that.abstractDescription != null)
            return false;
        if (topicGroupQuestions != null ? !topicGroupQuestions.equals(that.topicGroupQuestions) : that.topicGroupQuestions != null)
            return false;
//        if (concepts != null ? !concepts.equals(that.concepts) : that.concepts != null) return false;
        if (authors != null ? !authors.equals(that.authors) : that.authors != null) return false;
        if (otherMaterials != null ? !otherMaterials.equals(that.otherMaterials) : that.otherMaterials != null)
            return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;
    }

    @Override
    public int hashCode() {
        int result = abstractDescription != null ? abstractDescription.hashCode() : 0;
//        result = 31 * result + (topicGroupQuestions != null ? topicGroupQuestions.hashCode() : 0);
//        result = 31 * result + (concepts != null ? concepts.hashCode() : 0);
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + (otherMaterials != null ? otherMaterials.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "TopicGroupRevisionJson (abstractDescription=%s, topicQuestions=%s, concepts=%s, authors=%s, otherMaterials=%s, comments=%s)",
                this.abstractDescription, this.topicGroupQuestions, this.concepts, this.authors, this.otherMaterials, this.comments);
    }
}
