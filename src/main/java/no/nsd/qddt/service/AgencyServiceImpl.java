package no.nsd.qddt.service;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.repository.AgencyRepository;
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
public class AgencyServiceImpl implements AgencyService {

    private AgencyRepository agencyRepository;

    @Autowired
    AgencyServiceImpl(AgencyRepository agencyRepository){
        this.agencyRepository = agencyRepository;
    }



    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists(UUID uuid) {
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Agency findOne(UUID id) {
        return agencyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, Agency.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agency> findAll() {
        return agencyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Agency> findAll(Pageable pageable) {return agencyRepository.findAll(pageable);}

    @Override
    public List<Agency> findAll(Iterable<UUID> uuids) {
        return agencyRepository.findAll(uuids);
    }


    @Override
    @Transactional(readOnly = false)
    public Agency save(Agency instance) {

        instance.setCreated(LocalDateTime.now());
        return agencyRepository.save(instance);
    }

    @Override
    public void delete(UUID uuid) {
        agencyRepository.delete(uuid);
    }







}
