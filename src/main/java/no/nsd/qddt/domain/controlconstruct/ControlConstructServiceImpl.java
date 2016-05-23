package no.nsd.qddt.domain.controlconstruct;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("instrumentQuestionService")
class ControlConstructServiceImpl implements ControlConstructService {

    private ControlConstructRepository controlConstructRepository;

    @Autowired
    public ControlConstructServiceImpl(ControlConstructRepository controlConstructRepository) {
        this.controlConstructRepository = controlConstructRepository;
    }

    @Override
    public long count() {
        return controlConstructRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return controlConstructRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public ControlConstruct findOne(UUID id) {
        return controlConstructRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, ControlConstruct.class)
        );
    }

    @Override
    @Transactional(readOnly = false)
    public ControlConstruct save(ControlConstruct instance) {
        return controlConstructRepository.save(instance);
    }

    @Override
    public List<ControlConstruct> save(List<ControlConstruct> instances) {
        return controlConstructRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        controlConstructRepository.delete(uuid);
    }

    @Override
    public void delete(List<ControlConstruct> instances) {
        controlConstructRepository.delete(instances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ControlConstruct> findByInstrumentId(UUID instrumentId) {
        return controlConstructRepository.findByInstrumentId(instrumentId);
    }

    @Override
    public List<ControlConstruct> findByQuestionItemId(UUID questionItemId) {
        return controlConstructRepository.findByQuestionItemId(questionItemId);
    }

}
