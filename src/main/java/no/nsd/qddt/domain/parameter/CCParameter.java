package no.nsd.qddt.domain.parameter;


import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.envers.Audited;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author Stig Norland
 */


@Audited
@Entity
@Table(name = "CC_PARAMETER")
public class CCParameter extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    InOut direction;

    public InOut getDirection() {
        return direction;
    }

    public void setDirection(InOut direction) {
        this.direction = direction;
    }
}
