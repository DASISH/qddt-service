package no.nsd.qddt.domain.agency;

import no.nsd.qddt.classes.interfaces.IWebMenuPreview;
import no.nsd.qddt.classes.interfaces.Version;
import no.nsd.qddt.domain.surveyprogram.SurveyProgramJsonView;
import no.nsd.qddt.domain.user.json.UserJson;
import no.nsd.qddt.classes.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class AgencyEditJson implements Serializable, IWebMenuPreview {

private static final long serialVersionUID = 1L;
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Type(type="pg-uuid")
    private UUID id;
    private String name;
    private String description;
    private Timestamp modified;
    private Version version;
    private String classKind;
    private String defaultXmlLang;
    private List<SurveyProgramJsonView> surveyPrograms;
    private List<UserJson> users;



    public AgencyEditJson(Agency agency) {
        if (agency == null){
            LOG.error("Entity is null");
            StackTraceFilter.nsdStack().stream()
                .map(StackTraceElement::toString)
                .forEach(LOG::info);
            return;
        }
        setId(agency.getId());
        setName(agency.getName());
        setModified(agency.getModified());
        setVersion(agency.getVersion());
        setClassKind( agency.getClassKind() );
        defaultXmlLang = agency.getDefaultXmlLang();
        try {
            Hibernate.initialize( agency.getUsers() );
            setUsers( agency.getUsers().stream().map( UserJson::new ).collect( Collectors.toList()) );
        } catch (Exception ex) {
            LOG.error( "FEILA-01: " + agency.getId() + " - " , ex );
        }
        try {
            Hibernate.initialize( agency.getSurveyPrograms() );
            setSurveyPrograms( agency.getSurveyPrograms().stream().map( SurveyProgramJsonView::new ).collect( Collectors.toList())  );
        } catch (Exception ex) {
            LOG.error( "FEILA-02: " + agency.getId() + " - " , ex );
        }
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getClassKind() {
        return classKind;
    }

    public void setClassKind(String classKind) {
        this.classKind = classKind;
    }

    public String getDefaultXmlLang() {
        return defaultXmlLang;
    }

    public void setDefaultXmlLang(String defaultXmlLang) {
        this.defaultXmlLang = defaultXmlLang;
    }

    public List<SurveyProgramJsonView> getSurveyPrograms() {
        return surveyPrograms;
    }

    public void setSurveyPrograms(List<SurveyProgramJsonView> surveyPrograms) {
        this.surveyPrograms = surveyPrograms;
    }

    public List<UserJson> getUsers() {
        return users;
    }

    public void setUsers(List<UserJson> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgencyEditJson that = (AgencyEditJson) o;

        if (id != null ? !id.equals( that.id ) : that.id != null) return false;
        if (name != null ? !name.equals( that.name ) : that.name != null) return false;
        if (description != null ? !description.equals( that.description ) : that.description != null) return false;
        if (modified != null ? !modified.equals( that.modified ) : that.modified != null) return false;
        return defaultXmlLang != null ? defaultXmlLang.equals( that.defaultXmlLang ) : that.defaultXmlLang == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (defaultXmlLang != null ? defaultXmlLang.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"AgencyEditJson\", " +
            "\"id\":" + (id == null ? "null" : id) + ", " +
            "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
            "\"description\":" + (description == null ? "null" : "\"" + description + "\"") + ", " +
            "\"modified\":" + (modified == null ? "null" : modified) + ", " +
            "\"xmlLang\":" + (defaultXmlLang == null ? "null" : "\"" + defaultXmlLang + "\"") +
            "}";
    }


}
