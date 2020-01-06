package no.nsd.qddt.domain.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.IElementRef;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class QuestionPreview implements IElementRef {

    @JsonIgnore
    @Type(type="pg-uuid")
    @Column(name="questionitem_id")
    private UUID id;

    @Column(name = "questionitem_revision")
    private Integer revision;

    @Column(name = "question_name", length = 25)
    private String name;

    @Column(name = "question_text", length = 500 )
    private String text;

    @Override
    public ElementKind getElementKind() {
        return ElementKind.QUESTION_ITEM;
    }

    @Override
    public UUID getElementId() {
        return this.id;
    }

    public void setElementId(UUID refId) {
        this.id = refId;
    }

    @Override
    public Integer getElementRevision() {
        return this.revision;
    }

    public void setElementRevision(Integer revisionNumber) {
        this.revision = revisionNumber;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Version getVersion() {
        return null;
    }

    @Override
    public Object getElement() {
        return null;
    }

    public void setElement(Object element) {

    }
}
