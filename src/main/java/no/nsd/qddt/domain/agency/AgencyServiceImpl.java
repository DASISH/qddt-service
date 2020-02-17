package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.parentref.ConceptRef;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.user.UserService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static no.nsd.qddt.utils.FilterTool.defaultOrModifiedSort;
import static no.nsd.qddt.utils.FilterTool.defaultSort;
import static no.nsd.qddt.utils.StringTool.IsNullOrTrimEmpty;
import static no.nsd.qddt.utils.StringTool.likeify;

/**
 * @author Stig Norland
 */
@Service("agencyService")
class AgencyServiceImpl implements AgencyService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final AgencyRepository agencyRepository;
    private final UserService userService;

    @Autowired
    AgencyServiceImpl(AgencyRepository agencyRepository, UserService userService){
        this.agencyRepository = agencyRepository;
        this.userService = userService;
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
        return agencyRepository.save(instance);
    }


    @Override
    public void delete(UUID uuid) {
        agencyRepository.delete(uuid);
    }

    @Override
    public void delete(List<Agency> instances) {
        agencyRepository.delete(instances);
    }

    protected Agency prePersistProcessing(Agency instance) {
        return instance;
    }

    protected Agency postLoadProcessing(Agency instance) {
        assert  (instance != null);
        try {
            Hibernate.initialize(instance.getUsers());
        } catch (Exception ex){
            LOG.error("ConceptService.postLoadProcessing",ex);
        }
        return instance;
    }

}
