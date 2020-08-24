package no.nsd.qddt.domain.agency;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Stig Norland
 */
@Service("agencyService")
class AgencyServiceImpl implements AgencyService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final AgencyRepository agencyRepository;

    @Autowired
    AgencyServiceImpl(AgencyRepository agencyRepository) {
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
        return agencyRepository.findById(id).map( mapper -> {
            LOG.debug( " find one " );
           Hibernate.initialize(mapper.getSurveyPrograms());
           return  mapper;
        }).orElseThrow(
          () -> new ResourceNotFoundException(id, Agency.class)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agency> getAll() {
        return agencyRepository.findAll();
    }

    @Override
    public Page<Agency> findByNamePageable(String name, Pageable pageable) {

        return agencyRepository.findByNameLike(likeify(name), defaultSort(pageable,"name ASC") )
            .map( this::postLoadProcessing );
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<Agency> save(List<Agency> instances) {
        List<Agency> target = new ArrayList<>();
        agencyRepository.save(instances).forEach(target::add);
        return target;
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public Agency save(Agency instance) {
        Agency agent = agencyRepository.save(instance);
        Hibernate.initialize(agent.getSurveyPrograms());
        return  agent;
    }


    @Override
    public void delete(UUID uuid) {
        agencyRepository.delete(uuid);
    }


    protected Agency prePersistProcessing(Agency instance) {
        return instance;
    }

    protected Agency postLoadProcessing(Agency instance) {
        assert  (instance != null);
        try {
            Hibernate.initialize(instance.getUsers());
        } catch (Exception ex){
            LOG.error("AgencyService.postLoadProcessing",ex);
        }
        return instance;
    }

}
