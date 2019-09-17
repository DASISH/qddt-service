package no.nsd.qddt.domain.concept;


import java.util.UUID;

import javax.persistence.Embeddable;

import org.hibernate.envers.Audited;

import no.nsd.qddt.domain.elementref.AbstractElementRef;
import no.nsd.qddt.domain.elementref.ElementKind;
import no.nsd.qddt.domain.elementref.ElementRefTyped;
import no.nsd.qddt.domain.questionitem.QuestionItem;

/**
 * @author Stig Norland
 */
@Audited
@Embeddable
public class ConceptQuestionItemRef extends ElementRefTyped<QuestionItem> {

    public ConceptQuestionItemRef(UUID id, Integer rev) {
        super(ElementKind.QUESTION_ITEM, id, rev);
    }

    public ConceptQuestionItemRef(AbstractElementRef source) {
        super( source );
    }

    @Override
    public ConceptQuestionItemRef clone() {
        return (ConceptQuestionItemRef) super.clone();
    }
}