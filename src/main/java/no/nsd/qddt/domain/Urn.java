package no.nsd.qddt.domain;


import java.util.UUID;

/**
 * URN's are the ID's that are used for all entities that can be exported out of this solution.
 * This Solution will also need to keep track of these while importing entities from external systems,
 * so that entities will keep their relationship even when they are reintroduced into this system.
 *
 * @author Stig Norland
 */
public class Urn {

    private Agency agency;
    private UUID guid;
    private String version;

    public Urn(Agency agency, UUID guid, String version){
        this.agency = agency;
        this.guid = guid;
        this.version = version;
    }


    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
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
        if (guid != null ? !guid.equals(urn.guid) : urn.guid != null) return false;
        if (version != null ? !version.equals(urn.version) : urn.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = agency != null ? agency.hashCode() : 0;
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Urn{"+ agency.getName() +"-"+ guid.toString() + "-"+ version.toString() + '}';
    }
}
