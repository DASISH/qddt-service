package no.nsd.qddt.domain.agency;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("agencyService")
class AgencyServiceImpl implements AgencyService {

    private AgencyRepository agencyRepository;

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
        return agencyRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Agency findOne(UUID id) {
        return agencyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Agency.class)
        );
    }

    @Override
    @Transactional(readOnly = false)
    public Agency save(Agency instance) {
        return agencyRepository.save(instance);
    }

    @Override
    public List<Agency> save(List<Agency> instances) {
        return agencyRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        agencyRepository.delete(uuid);
    }

    @Override
    public void delete(List<Agency> instances) {
        agencyRepository.delete(instances);
    }
}
