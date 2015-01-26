package no.nsd.qddt.repository;

import no.nsd.qddt.domain.Question;
import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */
@Repository
public interface InstrumentQuestionRepository extends RevisionRepository<InstrumentQuestion, Long, Integer>, JpaRepository<InstrumentQuestion, Long> {

    public List<InstrumentQuestion> findByInstrument(Instrument instrument);

    public List<InstrumentQuestion> findByQuestion(Question question);

}
