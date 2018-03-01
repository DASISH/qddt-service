package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.embedded.ElementRef;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.refclasses.ConceptRef;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static no.nsd.qddt.domain.AbstractEntityAudit.ChangeKind;
import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ConceptRepository conceptRepository;
    private final ConceptAuditService auditService;
    private final QuestionItemAuditService questionAuditService;

    @Autowired
      ConceptServiceImpl(ConceptRepository conceptRepository
            , ConceptAuditService conceptAuditService
            , QuestionItemAuditService questionAuditService){
        this.conceptRepository = conceptRepository;
        this.auditService = conceptAuditService;
        this.questionAuditService = questionAuditService;
    }

    @Override
    public long count() {
        return conceptRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return conceptRepository.exists(uuid);
    }

    @Override
    public Concept findOne(UUID uuid) {
        return conceptRepository.findById(uuid).map(this::postLoadProcessing).orElseThrow(
//        return conceptRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public Concept save(Concept instance) {
        return postLoadProcessing(
            conceptRepository.save(
                prePersistProcessing(instance)));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER','ROLE_USER')")
    public List<Concept> save(List<Concept> instances) {
        instances.forEach(this::save);
        return instances;
    }

 /*    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
 */
    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public Concept copy(UUID id, Long rev, UUID parentId) {
        Concept source = auditService.findRevision(id,rev.intValue()).getEntity();

        Concept target = new ConceptFactory().copy(source, rev);
        target.setParentU(parentId);
        return conceptRepository.save(target);

    }


    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(UUID uuid) {
        conceptRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER')")
    public void delete(List<Concept> instances) {
        conceptRepository.delete(instances);
    }


    private Concept prePersistProcessing(Concept instance) {
        try {
            instance.getConceptQuestionItems().stream()
                .filter( f -> f.getRevisionNumber() == null )
                .forEach( cqi -> {
                    Revision<Integer, QuestionItem> rev = questionAuditService.findLastChange( cqi.getId() );
                    cqi.setRevisionNumber( rev.getRevisionNumber().longValue() );
                } );

            // children are saved to hold revision info... i guess, these saves shouldn't
            if (instance.isBasedOn() == false)
                instance.getChildren().stream().forEach( this::setChildChangeStatus );
        } catch (NullPointerException npe) {
            LOG.error("ConceptService-> prePersistProcessing " + npe);
            StackTraceFilter.filter(npe.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        } catch(Exception ex) {
            LOG.error("ConceptService-> prePersistProcessing " + instance.getName(), ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        return instance;
    }

    private void setChildChangeStatus(Concept concept){
        if (concept.getChangeKind() != ChangeKind.IN_DEVELOPMENT &&
                concept.getChangeKind() != ChangeKind.ARCHIVED ) {

            concept.setChangeKind(ChangeKind.UPDATED_PARENT);
            concept.setChangeComment("Touched to save...");
        }
    }

    /*
        post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
        thus we need to populate some elements ourselves.
     */
    protected Concept postLoadProcessing(Concept instance) {
        assert  (instance != null);
        try{
            for (ElementRef<QuestionItem> cqi :instance.getConceptQuestionItems()) {

                Revision<Integer, QuestionItem> rev = questionAuditService.getQuestionItemLastOrRevision(
                        cqi.getId(),
                        cqi.getRevisionNumber().intValue());

                cqi.setElement(rev.getEntity());
                cqi.getElementAs().setConceptRefs(
                    conceptRepository.findByConceptQuestionItemsQuestionId(cqi.getId()).stream()
                    .map( c -> new ConceptRef(c) )
                    .collect( Collectors.toList())
                );

                if (!cqi.getRevisionNumber().equals(rev.getRevisionNumber().longValue())) {
                    cqi.setRevisionNumber(rev.getRevisionNumber().longValue());
                }
            }
        } catch (Exception ex){
            LOG.error("ConceptService.postLoadProcessing",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        instance.getChildren().forEach(this::postLoadProcessing);
        return instance;
    }


    @Override
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,
                defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    public Page<Concept> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndBasedOnObjectIsNull(name,description,
                defaultSort(pageable,"name ASC"));
        pages.map(this::postLoadProcessing);
        return pages;
    }

    @Override
    public List<Concept> findByQuestionItem(UUID id) {
        return conceptRepository.findByConceptQuestionItemsQuestionId(id);
    }


}

