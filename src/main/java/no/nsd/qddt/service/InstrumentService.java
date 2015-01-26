package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.Instrument;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface InstrumentService {

    public Instrument findById(Long id);

    public List<Instrument> findAll();

    public Instrument save(Instrument instrument);
}
