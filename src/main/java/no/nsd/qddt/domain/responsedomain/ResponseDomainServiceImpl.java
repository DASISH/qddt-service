package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
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

    private final ResponseDomainRepository responseDomainRepository;
    private final ResponseDomainAuditService auditService;
    private final CategoryService categoryService;

    @Autowired
    public ResponseDomainServiceImpl(ResponseDomainRepository responseDomainRepository, CategoryService categoryService,ResponseDomainAuditService responseDomainAuditService) {
        this.responseDomainRepository = responseDomainRepository;
        this.categoryService = categoryService;
        this.auditService = responseDomainAuditService;
    }

    @Override
    public long count() {
        return responseDomainRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return responseDomainRepository.existsById(uuid);
    }

    @Override
    public ResponseDomain findOne(UUID uuid) {
        return responseDomainRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, ResponseDomain.class));
    }

    @Override
    @Transactional()
    public ResponseDomain save(ResponseDomain instance) {
        return postLoadProcessing(
                responseDomainRepository.save(
                        prePersistProcessing(instance)));
    }

//    @Override
//    public List<ResponseDomain> save(List<ResponseDomain> instances) {
//        instances.forEach(this::prePersistProcessing);
//        return responseDomainRepository.save(instances);
//    }

    @Override
    public void delete(UUID uuid) {
        responseDomainRepository.deleteById(uuid);
    }

    @Override
    public void delete(List<ResponseDomain> instances) {
        responseDomainRepository.deleteAll(instances);
    }


    private ResponseDomain prePersistProcessing(ResponseDomain instance) {

        if (instance.getManagedRepresentation().getId() == null) {
            instance.beforeUpdate();
            instance.setManagedRepresentation(
                categoryService.save(
                    instance.getManagedRepresentation()));
        } else
            instance.populateCodes();

        if(instance.isBasedOn()) {
            Long rev= auditService.findLastChange(instance.getId()).getRevisionNumber().get();
            instance.makeNewCopy(rev);
        } else if (instance.isNewCopy()) {
            instance.makeNewCopy(null);
        }
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

//    @Override
//    public ResponseDomain createMixed(UUID rdId, UUID missingId){
//        ResponseDomain old = findOne(rdId);
//        Category missing = categoryService.findOne(missingId);
//        Category mixedCa = new Category();
//
//        mixedCa.setName(old.getManagedRepresentation().getName() +" + " + missing.getName());
//        mixedCa.setCategoryType(CategoryType.MIXED);
//        mixedCa.addChild(old.getManagedRepresentation());
//        mixedCa.addChild(missing);
//
//        ResponseDomain mixedRd = new ResponseDomain();
//        mixedRd.setManagedRepresentation(mixedCa);
//        mixedRd.setName(old.getName() + " + " + missing.getName());
//        mixedRd.setDescription(old.getDescription() + System.lineSeparator() + missing.getDescription());
//        mixedRd.setResponseKind(ResponseKind.MIXED);
//        mixedRd.setDisplayLayout(old.getDisplayLayout());
//        mixedRd.setCodes(old.getCodes());
//
//        return  save(mixedRd);
//    }


    private String likeify(String value){
        value = value.replace("*", "%");
        if (!value.startsWith("%"))
            value = "%"+value;
        if (!value.endsWith("%"))
            value = value + "%";
        return value;
    }

}
