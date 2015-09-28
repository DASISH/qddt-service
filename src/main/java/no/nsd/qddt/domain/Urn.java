package no.nsd.qddt.domain;


import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import java.util.UUID;

/**
 * URN's are the ID's that are used for all entities that can be exported out of this solution.
 * This Solution will also need to keep track of these while importing entities from external systems,
 * so that entities will keep their relationship even when they are reintroduced into this system.
 *
 *
 *
 * @author Stig Norland
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class Urn {

    @Id
    @Column(name = "id")
    @Type(type="pg-uuid")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;
    private Agency agency;
    private String version;

    public Urn(){}

    public Urn(Agency agency, UUID id, String version){
        this.agency = agency;
        this.id = id;
        this.version = version;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID guid) {
        this.id = guid;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Urn urn = (Urn) o;

        if (agency != null ? !agency.equals(urn.agency) : urn.agency != null) return false;
        if (id != null ? !id.equals(urn.id) : urn.id != null) return false;
        if (version != null ? !version.equals(urn.version) : urn.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = agency != null ? agency.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Urn{" +
                "id=" + id +
                ", agency=" + agency +
                ", version='" + version + '\'' +
                '}';
    }
}
