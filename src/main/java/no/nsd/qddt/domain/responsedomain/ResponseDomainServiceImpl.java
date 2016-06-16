package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.code.Code;
import no.nsd.qddt.domain.code.CodeService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("responseDomainService")
class ResponseDomainServiceImpl implements ResponseDomainService {

    private ResponseDomainRepository responseDomainRepository;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository) {
        this.responseDomainRepository = responseDomainRepository;
    }

    @Override
    public long count() {
        return responseDomainRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return responseDomainRepository.exists(uuid);
    }

    @Override
    public ResponseDomain findOne(UUID uuid) {
        return responseDomainRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, ResponseDomain.class));
    }

    @Override
    @Transactional(readOnly = false)
    public ResponseDomain save(ResponseDomain instance) {
        instance.populateCodes();
        return responseDomainRepository.save(instance);
    }

    @Override
    public List<ResponseDomain> save(List<ResponseDomain> instances) {
        instances.forEach(rd->rd.populateCodes());
        return responseDomainRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        responseDomainRepository.delete(uuid);
    }

    @Override
    public void delete(List<ResponseDomain> instances) {
        responseDomainRepository.delete(instances);
    }

    @Override
    public Page<ResponseDomain> findBy(ResponseKind responseKind, String name, String description, Pageable pageable) {
        return responseDomainRepository.findByResponseKindAndNameLikeOrDescriptionLike(responseKind,name,description,pageable);
    }

    @Override
    public Page<ResponseDomain> findByQuestion(ResponseKind responseKind, String name, String question, Pageable pageable) {
        return  null;
    }


}
