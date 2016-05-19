package no.nsd.qddt.domain.questionItem.audit;

import no.nsd.qddt.domain.questionItem.QuestionItem;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface QuestionItemAuditRepository extends EnversRevisionRepository<QuestionItem, UUID, Integer> {

}

