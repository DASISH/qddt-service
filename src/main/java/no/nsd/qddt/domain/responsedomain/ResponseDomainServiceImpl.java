package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
//        System.out.println("Save ResponseDomain ->" + instance.getName());
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
        Page<ResponseDomain> pages = responseDomainRepository.findByResponseKindAndNameLikeAndDescriptionLike(responseKind,name,description,pageable);
        return pages;
    }

    @Override
    public Page<ResponseDomain> findByQuestion(ResponseKind responseKind, String name, String question, Pageable pageable) {
        return  null;
//        Page<ResponseDomain> pages = responseDomainRepository.findByResponseKindAndNameLikeOrQuestionItemQuestionQuestionLike(responseKind,name,question,pageable);
//        return pages;
    }


}
