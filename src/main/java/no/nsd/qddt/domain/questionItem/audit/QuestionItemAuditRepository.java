package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface QuestionItemAuditRepository extends RevisionRepository<QuestionItem, UUID, Long> {

    Page<Revision<Long,QuestionItem>> findRevisionsByIdAndChangeKindNotIn(UUID uuid, Collection<AbstractEntityAudit.ChangeKind> changeKinds, Pageable pageable);
}

