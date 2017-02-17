package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.category.CategoryService;
import no.nsd.qddt.domain.category.CategoryType;
import no.nsd.qddt.domain.responsedomain.audit.ResponseDomainAuditService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
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
    private ResponseDomainAuditService auditService;
    private CategoryService categoryService;

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
        instance = responseDomainRepository.save(makeCopy(instance));
        return instance;
    }

    @Override
    public List<ResponseDomain> save(List<ResponseDomain> instances) {
        instances.forEach(rd->{
            rd.populateCodes();
            makeCopy(rd);
        });
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

        Page<ResponseDomain> pages = responseDomainRepository.findByResponseKindAndNameIgnoreCaseLikeAndDescriptionIgnoreCaseLike(
                responseKind,
                likeify(name),
                likeify(description),
                pageable);
        return pages;
    }

    @Override
    public Page<ResponseDomain> findByQuestion(ResponseKind responseKind, String name, String question, Pageable pageable) {
        return  null;
//        Page<ResponseDomain> pages = responseDomainRepository.findByResponseKindAndNameLikeOrQuestionItemQuestionQuestionLike(responseKind,name,question,pageable);
//        return pages;
    }

    @Override
    public ResponseDomain createMixed(UUID rdId, UUID missingId){
        ResponseDomain old = findOne(rdId);
        Category missing = categoryService.findOne(missingId);
        Category mixedCa = new Category();

        mixedCa.setName(old.getManagedRepresentation().getName() +" + " + missing.getName());
        mixedCa.setCategoryType(CategoryType.MIXED);
        mixedCa.addChild((Category)old.getManagedRepresentation());
        mixedCa.addChild(missing);

        ResponseDomain mixedRd = new ResponseDomain();
        mixedRd.setManagedRepresentation(mixedCa);
        mixedRd.setName(old.getName() + " + " + missing.getName());
        mixedRd.setDescription(old.getDescription() + System.lineSeparator() + missing.getDescription());
        mixedRd.setResponseKind(ResponseKind.MIXED);
        mixedRd.setDisplayLayout(old.getDisplayLayout());
        mixedRd.setCodes(old.getCodes());

        return  save(mixedRd);
    }


    private ResponseDomain makeCopy(ResponseDomain source){

        if(source.isNewBasedOn()){

            Revision<Integer, ResponseDomain> lastChange = auditService.findLastChange(source.getId());
            ResponseDomain updated = lastChange.getEntity();
            updated.setAgency(source.getAgency());
            updated.setDescription(source.getDescription());
            updated.setName(source.getName());
            updated.setDisplayLayout(source.getDisplayLayout());
            updated.setCodes(source.getCodes());
            updated.setResponseCardinality(source.getResponseCardinality());
            updated.setResponseKind(source.getResponseKind());
            updated.setChangeComment(source.getChangeComment());
            updated.setChangeKind(source.getChangeKind());
            updated.setVersion(source.getVersion());
            updated.setModified(source.getModified());
            updated.setModifiedBy(source.getModifiedBy());
            Category mr = updated.getManagedRepresentation();
            mr.makeNewCopy(lastChange.getRevisionNumber());
            mr.setLabel("basedOn " + source.getId());
            updated.setManagedRepresentation(mr);
            updated.makeNewCopy(lastChange.getRevisionNumber());
            return updated;
        }

        if (source.getId() == null && source.getModified() != null) {
            source.makeNewCopy(null);
            Category mr = source.getManagedRepresentation();
            mr.makeNewCopy(null);
            mr.setLabel("basedOn " + source.getId());
            source.setManagedRepresentation(mr);
        }
        return source;
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
