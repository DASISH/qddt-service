package no.nsd.qddt.domain.question.audit;

import no.nsd.qddt.domain.question.Question;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface QuestionAuditRepository extends EnversRevisionRepository<Question, UUID, Integer> {

}

