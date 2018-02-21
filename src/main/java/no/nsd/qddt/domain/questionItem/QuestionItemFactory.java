package no.nsd.qddt.domain.questionItem;

import no.nsd.qddt.domain.IEntityFactory;

public class QuestionItemFactory implements IEntityFactory<QuestionItem> {

	@Override
	public QuestionItem create() {
		return new QuestionItem();
	}

	@Override
	public QuestionItem copyBody(QuestionItem source, QuestionItem dest) {
        dest.setName(source.getName());
        dest.setResponseDomain(source.getResponseDomain());
        dest.setResponseDomainRevision(source.getResponseDomainRevision());
        dest.setQuestion(source.getQuestion());
        dest.setIntent(source.getIntent());

        return dest;
	}
    
}