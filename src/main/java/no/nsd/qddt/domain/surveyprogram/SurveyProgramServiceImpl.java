package no.nsd.qddt.domain.surveyprogram;

import no.nsd.qddt.domain.concept.Concept;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.questionitem.audit.QuestionItemAuditService;
import no.nsd.qddt.domain.topicgroup.TopicGroup;
import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.exception.DescendantsArchivedException;
import no.nsd.qddt.exception.ResourceNotFoundException;
import no.nsd.qddt.exception.StackTraceFilter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("surveyProgramService")
class SurveyProgramServiceImpl implements SurveyProgramService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ElementLoader qiLoader;
    private final SurveyProgramRepository surveyProgramRepository;

    @Autowired
    public SurveyProgramServiceImpl(SurveyProgramRepository surveyProgramRepository,
                                    QuestionItemAuditService questionItemAuditService) {
        this.qiLoader = new ElementLoader( questionItemAuditService );
        this.surveyProgramRepository = surveyProgramRepository;
    }


    @Override
    public long count() {
        return surveyProgramRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return surveyProgramRepository.exists(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public SurveyProgram findOne(UUID uuid) {
        return postLoadProcessing(surveyProgramRepository.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException(uuid, SurveyProgram.class))
        );
    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')  and hasPermission(#instance,'AGENCY')")
    public SurveyProgram save(SurveyProgram instance) {
        try {
            return postLoadProcessing(
                    surveyProgramRepository.save(
                            prePersistProcessing(instance)));
        }catch (Exception ex){
            LOG.error("SAVING SURVEY FAILED ->",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map( StackTraceElement::toString )
                .forEach(LOG::info);
            throw ex;
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void delete(UUID uuid) {
        if (surveyProgramRepository.hasArchive( uuid.toString() ) > 0)
            throw new DescendantsArchivedException( uuid.toString() );
        surveyProgramRepository.delete(uuid);
    }

    @Override
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public List<SurveyProgram> findByAgency(User user) {
        return surveyProgramRepository.findByAgencyOrIsArchivedOrderByNameAsc(user.getAgency(), true)
            .stream().map(this::postLoadProcessing).collect(Collectors.toList());
    }


    private SurveyProgram prePersistProcessing(SurveyProgram instance) {
        return  doArchive( instance ) ;
    }

    private SurveyProgram postLoadProcessing(SurveyProgram instance) {
        assert  (instance != null);
        try{
            String tekst = (instance.getComments().size() > 0) ? " has comments": "has NOT comments";
            System.out.println(instance.getName() +  tekst);

            instance.getStudies().forEach( study ->
                Hibernate.initialize(study.getInstruments()));

            if (StackTraceFilter.stackContains("getPdf","getXml")) {
//                Hibernate.initialize(instance.getComments());
                instance.getStudies().forEach(  study ->
                    study.getTopicGroups().forEach( this::loadTopic ));
            }


        } catch (Exception ex){
            LOG.error("postLoadProcessing",ex);
        }
        return instance;
    }

    private void loadTopic(TopicGroup topic){
        topic.getTopicQuestionItems().forEach( qiLoader::fill );
//        Hibernate.initialize(topic.getComments());
        Hibernate.initialize(topic.getConcepts());
        LOG.debug("PDF -> fetching  concepts ");
        topic.getConcepts().forEach( this::loadConceptQuestion );
    }

    private void loadConceptQuestion(Concept parent) {
        parent.getChildren().forEach( this::loadConceptQuestion );
//        Hibernate.initialize(parent.getComments());
        parent.getConceptQuestionItems().forEach( qiLoader::fill );
    }

}
