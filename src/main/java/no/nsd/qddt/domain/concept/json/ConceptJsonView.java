package no.nsd.qddt.domain.concept.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.user.UserJson;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ConceptJsonView {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private String label;

    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    @Embedded
    private Version version;

    private List<ConceptQuestionJson> conceptQuestionItems = new ArrayList<>();

    private Set<CommentJsonEdit> comments = new HashSet<>();

    private Set<ConceptJsonView> children = new HashSet<>();

    private TopicRef topicRef;


    public ConceptJsonView(Concept concept) {
        setId(concept.getId());
        setName(concept.getName());
        setLabel(concept.getLabel());
        setDescription(concept.getDescription());
        modifiedBy = new UserJson(concept.getModifiedBy());
        agency = new AgencyJsonView(concept.getAgency());
        modified = concept.getModified();
        version = concept.getVersion();
        setChildren(concept.getChildren().stream().map(ConceptJsonView::new).collect(Collectors.toSet()));
        setConceptQuestionItems(concept.getConceptQuestionItems().stream().map(ConceptQuestionJson::new)
                .collect(Collectors.toList()));
        setComments(concept.getComments().stream().map(CommentJsonEdit::new).collect(Collectors.toSet()));
        setTopicRef(concept.getTopicRef());
    }

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    private void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public Version getVersion() {
        return version;
    }

    public UserJson getModifiedBy() {
        return modifiedBy;
    }

    public AgencyJsonView getAgency() {
        return agency;
    }
    
    public List<ConceptQuestionJson> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    private void setConceptQuestionItems(List<ConceptQuestionJson> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }


    public Set<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(Set<CommentJsonEdit> comments) {
        this.comments = comments;
    }

    public Set<ConceptJsonView> getChildren() {
        return children;
    }

    private void setChildren(Set<ConceptJsonView> children) {
        this.children = children;
    }

    public TopicRef getTopicRef() {
        return topicRef;
    }

    private void setTopicRef(TopicRef topicRef) {
        this.topicRef = topicRef;
    }
}
