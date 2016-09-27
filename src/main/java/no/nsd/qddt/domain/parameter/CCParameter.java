package no.nsd.qddt.domain.parameter;


import no.nsd.qddt.domain.AbstractEntityAudit;
import org.hibernate.envers.Audited;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Stig Norland
 */


@Audited
@Entity
@Table(name = "PARAMETER")
public class CCParameter extends AbstractEntityAudit {

}
