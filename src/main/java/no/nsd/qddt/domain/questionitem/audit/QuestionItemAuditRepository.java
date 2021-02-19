package no.nsd.qddt.domain.questionitem.audit;

import no.nsd.qddt.domain.questionitem.QuestionItem;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface QuestionItemAuditRepository extends RevisionRepository<QuestionItem, UUID, Integer> {

}

