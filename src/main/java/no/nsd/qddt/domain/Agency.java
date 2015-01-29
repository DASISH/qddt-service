package no.nsd.qddt.domain;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.response.ResponseDomain;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * The agency expressed as filed with the DDI Agency ID Registry with optional additional sub-agency extensions.
 * The length restriction of the complete string is done with the means of minLength and maxLength.
 * The regular expression engine of XML Schema has no lookahead possibility.
 *
 * We'll have a relationship with surveys and groups
 *
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "agency")
public class Agency extends AbstractEntity {


    //UUID part of the URN, saves as binary for most db's (PostgreSQL, SQL Server have native types)
    @Column(name = "guid", columnDefinition = "BINARY(16)")
    private UUID guid = UUID.randomUUID();


    @Column(name = "name")
    private String name;


//    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
//    private Set<Survey> surveys = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<ResponseDomain> responses = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();


    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agency agency = (Agency) o;

        if (super.getCreated() != null ? !super.getCreated().equals(agency.getCreated()) : agency.getCreated() != null) return false;
        if (super.getCreatedBy() != null ? !super.getCreatedBy().equals(agency.getCreatedBy()) : agency.getCreatedBy() != null) return false;
        if (guid != null ? !guid.equals(agency.guid) : agency.guid != null) return false;
        if (super.getId() != null ? !super.getId().equals(agency.getId()) : agency.getId() != null) return false;
        if (name != null ? !name.equals(agency.name) : agency.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.getId() != null ? super.getId().hashCode() : 0;
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        result = 31 * result + (super.getCreated() != null ? super.getCreated().hashCode() : 0);
        result = 31 * result + (super.getCreatedBy() != null ? super.getCreatedBy().hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + super.getId() +
                ", guid=" + guid +
                ", created=" + super.getCreated() +
                ", createdBy=" + super.getCreatedBy() +
                ", name='" + name + '\'' +
                '}';
    }
}
