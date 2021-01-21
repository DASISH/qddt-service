package no.nsd.qddt.domain.concept.json;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.classes.elementref.ElementRefEmbedded;
import no.nsd.qddt.domain.classes.elementref.ParentRef;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
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
    private final Timestamp modified;

    private final UserJson modifiedBy;

    private final AgencyJsonView agency;

    @Embedded
    private final Version version;

    private List<ElementRefEmbedded<QuestionItem>> conceptQuestionItems = new ArrayList<>();

    private List<CommentJsonEdit> comments = new ArrayList<>();

    private Set<ConceptJsonView> children = new HashSet<>();

	  private final String classKind;

    private ParentRef<TopicGroup> parentRef;


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
        setParentRef(concept.getParentRef());
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

    public List<ElementRefEmbedded<QuestionItem>> getConceptQuestionItems() {
        return conceptQuestionItems;
    }

    public void setConceptQuestionItems(List<ElementRefEmbedded<QuestionItem>> conceptQuestionItems) {
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

    public ParentRef<TopicGroup> getParentRef() {
        return parentRef;
    }

    public void setParentRef(ParentRef<TopicGroup> parentRef) {
        this.parentRef = parentRef;
    }

    public String getClassKind() {
        return classKind;
    }
}
