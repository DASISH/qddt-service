package no.nsd.qddt.domain.concept.json;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementRef;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.refclasses.TopicRef;
import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import java.sql.Timestamp;
import java.util.*;
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

    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    // @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private Timestamp modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    @Embedded
    private Version version;

    private List<ElementRef> conceptQuestionItems = new ArrayList<>();

    private List<CommentJsonEdit> comments = new ArrayList<>();

    private Set<ConceptJsonView> children = new HashSet<>();

    private TopicRef topicRef;

	private String classKind;


    public ConceptJsonView(Concept concept) {
        setId(concept.getId());
        setName(concept.getName());
        setLabel(concept.getLabel());
        setDescription(concept.getDescription());
        modifiedBy = concept.getModifiedBy();
        agency = new AgencyJsonView(concept.getAgency());
        modified = concept.getModified();
        version = concept.getVersion();
        setChildren(concept.getChildren().stream().map(ConceptJsonView::new).collect(Collectors.toSet()));
        setConceptQuestionItems(concept.getConceptQuestionItems());
        setComments(concept.getComments());
        setTopicRef(concept.getTopicRef());
        classKind = concept.getClassKind();
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

    public Timestamp getModified() {
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
    
    public List<ElementRef> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    private void setConceptQuestionItems(List<ElementRef> conceptQuestionItems) {
        this.conceptQuestionItems = conceptQuestionItems;
    }


    public List<CommentJsonEdit> getComments() {
        return comments;
    }

    private void setComments(List<CommentJsonEdit> comments) {
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

    public String getClassKind() {
        return classKind;
    }
}
