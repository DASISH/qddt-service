package no.nsd.qddt.domain.auditmap;

import no.nsd.qddt.domain.questionitem.QuestionItem;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Entity
@DiscriminatorValue( "QUESTION_ITEM")
public class QuestionItemAuditMap extends AbstractAuditMap<QuestionItem> {

    public QuestionItemAuditMap() {
    }

    public QuestionItemAuditMap( UUID id, Integer rev) {
        super(  id, rev );
    }

    @Override
    public QuestionItemAuditMap clone() {
        QuestionItemAuditMap retval = new QuestionItemAuditMap( getElementId(),getElementRevision());
        retval.setVersion( getVersion() );
        retval.setName( getName() );
        return retval;
    }
}
