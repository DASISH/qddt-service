package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.BaseServiceAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.Collection;
import java.util.UUID;


/**
 * @author Dag Østgulen Heradstveit
 */
public interface QuestionItemAuditService extends BaseServiceAudit<QuestionItem, UUID, Long> {

    Page<Revision<Long, QuestionItem>> findRevisionByIdAndChangeKindNotIn(UUID id, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);

    Revision<Long, QuestionItem> getQuestionItemLastOrRevision(UUID id, Long revision);

}
