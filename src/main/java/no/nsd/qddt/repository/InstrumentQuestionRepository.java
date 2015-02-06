package no.nsd.qddt.repository;

import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface InstrumentQuestionRepository extends BaseRepository<InstrumentQuestion>,
        EnversRevisionRepository<InstrumentQuestion, Long, Integer> {

    /**
     *
     * @param instrumentId
     * @return
     */
    List<InstrumentQuestion> findByInstrumentId(Long instrumentId);

    /**
     *
     * @param questionId
     * @return
     */
    List<InstrumentQuestion> findByQuestionId(Long questionId);

}
