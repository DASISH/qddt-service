package no.nsd.qddt.service;

import no.nsd.qddt.domain.Agency;
import no.nsd.qddt.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = false)
    public Agency save(Agency code) {
        return agencyRepository.save(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Revision<Integer, Agency> findLastChange(Long id) {
        return agencyRepository.findLastChangeRevision(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Revision<Integer, Agency>> findAllRevisionsPageable(Agency agency, Pageable pageable) {
        return agencyRepository.findRevisions(agency.getId(),pageable);
    }
}
