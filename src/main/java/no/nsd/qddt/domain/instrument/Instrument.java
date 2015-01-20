package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.AbstractEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "instrument")
public class Instrument extends AbstractEntity{

    //TODO implement


}
