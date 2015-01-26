package no.nsd.qddt.service;

import no.nsd.qddt.domain.instrument.Instrument;
import no.nsd.qddt.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
@Service("instrumentService")
public class InstrumentServiceImpl implements InstrumentService {

    private InstrumentRepository instrumentRepository;

    @Autowired
    public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Instrument findById(Long id) {
        return instrumentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Instrument save(Instrument instrument) {
        return instrumentRepository.save(instrument);
    }
}
