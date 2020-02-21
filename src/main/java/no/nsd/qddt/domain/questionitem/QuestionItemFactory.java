package no.nsd.qddt.domain.questionitem;

import no.nsd.qddt.domain.IEntityFactory;

/**
 * @author Stig Norland
 **/


public class QuestionItemFactory implements IEntityFactory<QuestionItem> {

	@Override
	public QuestionItem create() {
		return new QuestionItem();
	}

	@Override
	public QuestionItem copyBody(QuestionItem source, QuestionItem dest) {
        dest.setName(source.getName());
        dest.setResponseDomainRef( source.getResponseDomainRef() );
        dest.setQuestion(source.getQuestion());
        dest.setIntent(source.getIntent());

        return dest;
	}
    
}
