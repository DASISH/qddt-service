package no.nsd.qddt.domain.questionitem2.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.questionitem2.QuestionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;


/**
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionItemAuditService extends BaseServiceAudit<QuestionItem, UUID, Integer> {

    Page<Revision<Integer, QuestionItem>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

    Revision<Integer, QuestionItem> getQuestionItemLastOrRevision(UUID id, Integer revision);

}