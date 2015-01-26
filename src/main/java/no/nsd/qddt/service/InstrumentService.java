package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.Instrument;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentService {

    public Instrument findById(Long id);

    public List<Instrument> findAll();

    public Instrument save(Instrument instrument);

    public Revision<Integer, Instrument> findLastChange(Long id);

    public Page<Revision<Integer, Instrument>> findAllRevisionsPageable(Instrument instrument , int min, int max);
}
