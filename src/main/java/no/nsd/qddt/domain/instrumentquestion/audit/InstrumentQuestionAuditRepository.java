package no.nsd.qddt.domain.instrumentquestion.audit;

import no.nsd.qddt.domain.instrumentquestion.InstrumentQuestion;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstrumentQuestionAuditRepository extends EnversRevisionRepository<InstrumentQuestion, UUID, Integer> {

}