package no.nsd.qddt.domain.agency;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("agencyService")
class AgencyServiceImpl implements AgencyService {

    private final AgencyRepository agencyRepository;

    @Autowired
    AgencyServiceImpl(AgencyRepository agencyRepository){
        this.agencyRepository = agencyRepository;
    }

    @Override
    public long count() {
        return agencyRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return agencyRepository.existsById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Agency findOne(UUID id) {
        return agencyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Agency.class)
        );
    }

//    @Override
//    public List<Agency> save(List<Agency> instances) {
//        List<Agency> target = new ArrayList<>();
//        agencyRepository.saveAll(instances).forEach(target::add);
//        return target;
//    }

    @Override
    @Transactional()
    public Agency save(Agency instance) {
        return agencyRepository.save(instance);
    }


    @Override
    public void delete(UUID uuid) {
        agencyRepository.deleteById(uuid);
    }

    @Override
    public void delete(List<Agency> instances) {
        agencyRepository.deleteAll(instances);
    }

    protected Agency prePersistProcessing(Agency instance) {
        return instance;
    }

    protected Agency postLoadProcessing(Agency instance) {
        return instance;
    }
}
