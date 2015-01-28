package no.nsd.qddt.service;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Stig Norland
 */
@Service("agencyService")
public class AgencyServiceImpl implements AgencyService{

    private AgencyRepository agencyRepository;

    @Autowired
    AgencyServiceImpl(AgencyRepository agencyRepository){
        this.agencyRepository = agencyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Agency findById(Long id) {
        return agencyRepository.findOne(id);
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
    @Transactional(readOnly = false)
    public Agency save(Agency instance) {

        instance.setCreated(LocalDateTime.now());
        return agencyRepository.save(instance);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Agency value) { agencyRepository.delete(value);   }

}
