package no.nsd.qddt.domain.controlconstruct.pojo;

import no.nsd.qddt.domain.elementref.AbstractElementRef;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;

/**
 * @author Stig Norland
 */
@Embeddable
@Audited
public class ControlConstructRef <T extends QuestionConstruct> extends AbstractElementRef<T> {

}
