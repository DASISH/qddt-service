package no.nsd.qddt.domain.instrument;

import no.nsd.qddt.domain.BaseRepository;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Repository
interface InstrumentQuestionRepository extends BaseRepository<InstrumentQuestion,UUID>,EnversRevisionRepository<InstrumentQuestion, UUID, Integer> {

    /**
     *
     * @param instrumentId
     * @return
     */
    List<InstrumentQuestion> findByInstrumentId(UUID instrumentId);

    /**
     *
     * @param questionId
     * @return
     */
    List<InstrumentQuestion> findByQuestionId(UUID questionId);


    /**
     *
     * @param instructionId
     * @return
     */
    List<InstrumentQuestion> findByInstructionId(UUID instructionId);


}
