package no.nsd.qddt.domain.concept.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import no.nsd.qddt.domain.concept.ConceptQuestionItemRev;
import no.nsd.qddt.domain.questionItem.json.QuestionItemJsonView;

/**
 * @author Stig Norland
 */
public class ConceptQuestionJson {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private QuestionItemJsonView questionItem;

    private Integer questionItemRevision;

    public ConceptQuestionJson(ConceptQuestionItemRev q) {
        if (q == null) {
            LOG.info("ConceptQuestionItem is null");
            return;
        }
        setQuestionItem(new QuestionItemJsonView(q.getQuestionItem()));
        setQuestionItemRevision(q.getQuestionItemRevision().intValue());

    }


    public QuestionItemJsonView getQuestionItem() {
        return questionItem;
    }

    private void setQuestionItem(QuestionItemJsonView questionItem) {
        this.questionItem = questionItem;
    }

    public Integer getQuestionItemRevision() {
        return questionItemRevision;
    }

    private void setQuestionItemRevision(Integer questionItemRevision) {
        this.questionItemRevision = questionItemRevision;
    }

}
