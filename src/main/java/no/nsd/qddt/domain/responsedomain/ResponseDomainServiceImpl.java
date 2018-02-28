package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final ResponseDomainRepository responseDomainRepository;
    private final ResponseDomainAuditService auditService;
//    private final CategoryService categoryService;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository, ResponseDomainAuditService responseDomainAuditService) {
        this.responseDomainRepository = responseDomainRepository;
//        this.categoryService = categoryService;
        this.auditService = responseDomainAuditService;
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
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public ResponseDomain save(ResponseDomain instance) {
        return postLoadProcessing(
                responseDomainRepository.save(
                        prePersistProcessing(instance)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<ResponseDomain> save(List<ResponseDomain> instances) {
        instances.forEach(this::prePersistProcessing);
        return responseDomainRepository.save(instances);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        responseDomainRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<ResponseDomain> instances) {
        responseDomainRepository.delete(instances);
    }


    private ResponseDomain prePersistProcessing(ResponseDomain instance) {
        
        ResponseDomainFactory rdf= new ResponseDomainFactory();
        if(instance.isBasedOn()) {
            Long rev= auditService.findLastChange(instance.getId()).getRevisionNumber().longValue();
            instance = rdf.copy(instance, rev );
        } else if (instance.isNewCopy()) {
            instance = rdf.copy(instance, null);
        }
        // read the codes from the MR, into the RD
        if (instance.getCodes().size() == 0)
            instance.populateCodes();



//        if (instance.getManagedRepresentation().getId() == null) {
//            instance.beforeUpdate();
//            instance.setManagedRepresentation(
//                categoryService.save(
//                    instance.getManagedRepresentation()));
//        } else

        //instance.getManagedRepresentation().setChangeComment(instance.getChangeComment());

        return instance;
    }


    private ResponseDomain postLoadProcessing(ResponseDomain instance) {
        return instance;
    }

    @Override
    public Page<ResponseDomain> findBy(ResponseKind responseKind, String name, String description, Pageable pageable) {

        return responseDomainRepository.findByResponseKindAndNameIgnoreCaseLikeAndDescriptionIgnoreCaseLike(
                responseKind,
                likeify(name),
                likeify(description),
                pageable);
    }

    @Override
    public Page<ResponseDomain> findByQuestion(ResponseKind responseKind,  String question, Pageable pageable) {
        return
            responseDomainRepository.findByResponseKindAndNameLikeOrQuestionItemsQuestionLike(
                responseKind,
                likeify(question),
                pageable);
    }


    private String likeify(String value){
        value = value.replace("*", "%");
        if (!value.startsWith("%"))
            value = "%"+value;
        if (!value.endsWith("%"))
            value = value + "%";
        return value;
    }

}
