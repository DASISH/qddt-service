package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.classes.interfaces.IWebMenuPreview;
import no.nsd.qddt.domain.classes.interfaces.Version;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class SurveyProgramJsonView implements IWebMenuPreview {

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    private Timestamp modified;

    private Version version;

    public SurveyProgramJsonView() {
    }

    public SurveyProgramJsonView(SurveyProgram surveyProgram) {
        this.id = surveyProgram.getId();
        this.name = surveyProgram.getName();
        this.modified = surveyProgram.getModified();
        this.version = surveyProgram.getVersion();
    }


    public UUID getId() {
        return id;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyProgramJsonView that = (SurveyProgramJsonView) o;

        if (id != null ? !id.equals( that.id ) : that.id != null) return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        return modified != null ? modified.equals( that.modified ) : that.modified == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"SurveyProgramJsonView\", " +
            "\"id\":" + (id == null ? "null" : id) + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"modified\":" + (modified == null ? "null" : modified) +
            "}";
    }


}
