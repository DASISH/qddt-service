package no.nsd.qddt.domain.topicgroup.json;

import no.nsd.qddt.domain.BaseJsonEdit;
import no.nsd.qddt.domain.author.Author;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.json.ConceptJsonView;
import no.nsd.qddt.domain.othermaterial.OtherMaterialT;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroupquestionitem.TopicGroupQuestionItemJson;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class TopicGroupRevisionJson extends BaseJsonEdit {

    private static final long serialVersionUID = 2435677656441L;

	private String abstractDescription;

    private Set<TopicGroupQuestionItemJson> topicQuestionItems;

    private Set<ConceptJsonView> concepts = new HashSet<>();

    private Set<Author> authors = new HashSet<>();

    private Set<OtherMaterialT> otherMaterials = new HashSet<>();

    private boolean isArchived;

    private Set<CommentJsonEdit> comments = new HashSet<>();

    public TopicGroupRevisionJson(TopicGroup topicGroup) {
        super(topicGroup);
        if (topicGroup == null) return;
        setAbstractDescription(topicGroup.getAbstractDescription());
        setTopicQuestionItems( topicGroup.getTopicQuestionItems().stream().map(TopicGroupQuestionItemJson::new).collect(Collectors.toSet()));
        setAuthors(topicGroup.getAuthors());
        setOtherMaterials(topicGroup.getOtherMaterials());
        setArchived(topicGroup.isArchived());
        setComments(topicGroup.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
//        System.out.println("TopicGroupRevisionJson");
        setConcepts(topicGroup.getConcepts().stream().map(ConceptJsonView::new).collect(Collectors.toSet()));

    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    private void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }

    public Set<TopicGroupQuestionItemJson> getTopicQuestionItems() {
        return topicQuestionItems;
    }

    public void setTopicQuestionItems(Set<TopicGroupQuestionItemJson> topicQuestionItems) {
        this.topicQuestionItems = topicQuestionItems;
    }

    public Set<ConceptJsonView> getConcepts() {
        return concepts;
    }

    private void setConcepts(Set<ConceptJsonView> concepts) {
        this.concepts = concepts;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    private void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<OtherMaterialT> getOtherMaterials() {
        return otherMaterials;
    }

    private void setOtherMaterials(Set<OtherMaterialT> otherMaterials) {
        this.otherMaterials = otherMaterials;
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
        if (!(o instanceof TopicGroupRevisionJson)) return false;

        TopicGroupRevisionJson that = (TopicGroupRevisionJson) o;

        if (abstractDescription != null ? !abstractDescription.equals(that.abstractDescription) : that.abstractDescription != null)
            return false;
        if (topicQuestionItems != null ? !topicQuestionItems.equals(that.topicQuestionItems) : that.topicQuestionItems != null)
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
                this.abstractDescription, this.topicQuestionItems, this.concepts, this.authors, this.otherMaterials, this.comments);
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
