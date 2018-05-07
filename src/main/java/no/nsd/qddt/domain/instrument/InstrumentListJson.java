package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.user.UserJson;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import javax.xml.stream.events.EndDocument;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class InstrumentListJson {

    @Type(type = "pg-uuid")
    private UUID id;

	private String label;

    private String name;

    private String description;

    private String classKind;
 
    private Timestamp modified;

    private UserJson modifiedBy;

    private String instrumentKind;

    @Embedded
    private Version version;


    public InstrumentListJson() {
    }

    public InstrumentListJson(Instrument entity) {
        if (entity == null) return;
        id = entity.getId();
        name = entity.getName();
        label = entity.getLabel();
        description = entity.getDescription();
        version = entity.getVersion();
        modified = entity.getModified();
        modifiedBy = new UserJson(entity.getModifiedBy());
        classKind = entity.getClassKind();
        instrumentKind = entity.getInstrumentKind().getName();
    }

    public UUID getId() {
        return id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the kind
     */
    public String getInstrumentKind() {
        return instrumentKind;
    }

    public Timestamp getModified() {
        return modified;
    }


    public UserJson getModifiedBy() {
        return modifiedBy;
    }


    public Version getVersion() {
        return version;
    }

    public String getClassKind() {
        return classKind;
    }

}