package no.nsd.qddt.domain.concept;

import no.nsd.qddt.domain.concept.audit.ConceptAuditService;
import no.nsd.qddt.domain.conceptquestionitem.ConceptQuestionItem;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.topicgroup.TopicGroupService;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static no.nsd.qddt.utils.FilterTool.defaultSort;

/**
 * @author Stig Norland
 * @author Dag Østgulen Heradstveit
 */

@Service("conceptService")
class ConceptServiceImpl implements ConceptService {

    private ConceptRepository conceptRepository;
    private ConceptAuditService auditService;
    private QuestionItemAuditService questionAuditService;
    private TopicGroupService topicGroupService;

    @Autowired
      ConceptServiceImpl(ConceptRepository conceptRepository,ConceptAuditService conceptAuditService, TopicGroupService topicGroupService, QuestionItemAuditService questionAuditService){
        this.conceptRepository = conceptRepository;
        this.auditService = conceptAuditService;
        this.topicGroupService = topicGroupService;
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
        return conceptRepository.findById(uuid).map(c->populateRevisionedQI(c)).orElseThrow(
//        return conceptRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Concept.class));

    }

    @Override
    @Transactional()
    public Concept save(Concept instance) {
        System.out.println("Saving: " + instance.getName());
        instance =  conceptRepository.save(makeCopy(instance));
//        return instance;
        return populateRevisionedQI(instance);
    }

    @Override
    @Transactional()
    public List<Concept> save(List<Concept> instances) {
        instances.stream().forEach(c-> save(c));
        return instances;
    }

    @Override
    public void delete(UUID uuid) {
        conceptRepository.delete(uuid);
    }

    @Override
    public void delete(List<Concept> instances) {
        conceptRepository.delete(instances);
    }


    @Override
    public Page<Concept> findAllPageable(Pageable pageable) {
        Page<Concept> pages = conceptRepository.findAll(defaultSort(pageable,"name","modified DESC"));
        pages.map(c->populateRevisionedQI(c));
        return pages;
    }

    @Override
    public Page<Concept> findByTopicGroupPageable(UUID id, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByTopicGroupIdAndNameIsNotNull(id,
                defaultSort(pageable,"name","modified DESC"));
        pages.map(c->populateRevisionedQI(c));
        return pages;
    }

    @Override
    public Page<Concept> findByNameAndDescriptionPageable(String name, String description, Pageable pageable) {
        Page<Concept> pages = conceptRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(name,description,
                defaultSort(pageable,"name","modified DESC"));
        pages.map(c->populateRevisionedQI(c));
        return pages;
    }

    /*
        this function will make a copy of source and all it's child objects.

     */
    private Concept makeCopy(Concept source){

        source = harvestRevisionedQI(source);

        if(source.isNewBasedOn()){

            Revision<Integer, Concept> lastChange = auditService.findLastChange(source.getId());
            Concept updated = lastChange.getEntity();
            updated.setAgency(source.getAgency());
            updated.setDescription(source.getDescription());
            updated.setLabel(source.getLabel());
            updated.setName(source.getName());
            updated.setChangeComment(source.getChangeComment());
            updated.setChangeKind(source.getChangeKind());
            updated.setVersion(source.getVersion());
            updated.setModified(source.getModified());
            updated.setModifiedBy(source.getModifiedBy());

            if (source.getTopicGroup() != null)
              updated.setTopicGroup(source.getTopicGroup());

            updated.makeNewCopy(lastChange.getRevisionNumber());
            return updated;
        }

        if (source.getId() == null & source.getTopicRef().getId() != null) {
            TopicGroup tg = topicGroupService.findOne(source.getTopicRef().getId());
            source.setTopicGroup(tg);
            source.makeNewCopy(null);
        }
        return source;
    }



    /*
    post fetch processing, some elements are not supported by the framework (enver mixed with jpa db queries)
    thus we need to populate some elements ourselves.
     */
    private Concept populateRevisionedQI(Concept instance){
        assert  (instance != null);
        try{
            System.out.println("populateRevisionedQI " + instance.getName());
            // this work as long as instance.getQuestionItems() hasn't been called yet for this instance
            for (ConceptQuestionItem cqi :instance.getConceptQuestionItems()) {
                if (cqi.getQuestionItem().getVersion().getRevision() != null) {
                    System.out.println("Get revision "  +cqi.getQuestionItem().getVersion().getRevision());
                    cqi.setQuestionItem(questionAuditService.findRevision(
                            cqi.getQuestionItem().getId(),
                            cqi.getQuestionItemRevision())
                            .getEntity());
                }
                else {
                    cqi.getQuestionItem().getVersion().setRevision(questionAuditService.findLastChange(cqi.getQuestionItem().getId()).getRevisionNumber());
                    System.out.println("sat revision " + cqi.getQuestionItem().getVersion().getRevision());
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;
    }


    private Concept harvestRevisionedQI(Concept instance){
        assert  (instance != null);
        try{
            if (instance.getConceptQuestionItems().size() == 0 &&
                    instance.getQuestionItems().size() > 0) {
                System.out.println("harvestRevisionedQI -> Load from DB 'questionItem'");
                Set<ConceptQuestionItem> itemSet=  conceptRepository.findOne(instance.getId()).getConceptQuestionItems();
                instance.setConceptQuestionItems(itemSet); //LOAD questionitems from DB if empty list
            }
            else
                System.out.println("CQI:" + instance.getConceptQuestionItems().size() + " - QI:" +instance.getQuestionItems().size() );

            instance.getQuestionItems().forEach(qi-> {
                if (!instance.getConceptQuestionItems().stream().anyMatch(cqi->cqi.getQuestionItem().getId().equals(qi.getId()))) {
                    qi.getVersion().setRevision(questionAuditService.findLastChange(qi.getId()).getRevisionNumber());
                    instance.getConceptQuestionItems().add(new ConceptQuestionItem(instance, qi));
                }});

            instance.getConceptQuestionItems().forEach(q-> System.out.println(q.getQuestionItem().getName() +" - " + q.getQuestionItem().getVersion().getRevision()));
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return instance;

    }

}
