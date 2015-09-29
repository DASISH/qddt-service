package no.nsd.qddt.domain;


import no.nsd.qddt.domain.response.ResponseDomain;
import org.hibernate.envers.Audited;

import javax.persistence.*;
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
@Table(name = "AGENCY")
public class Agency extends AbstractEntity {


    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<ResponseDomain> responses = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<Concept> concepts = new HashSet<>();

    @OneToMany(mappedBy="agency", cascade = CascadeType.ALL)
    private Set<TopicGroup> topicGroups = new HashSet<>();



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
        if (!super.equals(o)) return false;

        Agency agency = (Agency) o;

        return !(name != null ? !name.equals(agency.name) : agency.name != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agency{" +
                ", created=" + super.getCreated() +
                ", createdBy=" + super.getCreatedBy() +
                ", name='" + name + '\'' +
                '}';
    }
}
