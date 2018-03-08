package no.nsd.qddt.domain.study;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.elementref.ElementLoader;
import no.nsd.qddt.domain.questionItem.audit.QuestionItemAuditService;
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

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
@Service("studyService")
class StudyServiceImpl implements StudyService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ElementLoader qiLoader;

    private final StudyRepository studyRepository;

    @Autowired
    public StudyServiceImpl(StudyRepository studyRepository,
                            QuestionItemAuditService questionItemAuditService) {
        this.qiLoader = new ElementLoader( questionItemAuditService );
        this.studyRepository = studyRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public long count() {
        return studyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(UUID uuid) {
        return studyRepository.exists(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR','ROLE_CONCEPT','ROLE_VIEW')")
    public Study findOne(UUID uuid) {
        return studyRepository.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException(uuid, Study.class)
        );    }


    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public Study save(Study instance) {
        return postLoadProcessing(
                studyRepository.save(
                        prePersistProcessing(instance)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public List<Study> save(List<Study> instances) {
        return studyRepository.save(instances);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void delete(UUID uuid) {

        studyRepository.delete(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void delete(List<Study> instances) {
        studyRepository.delete(instances);
    }

    private Study prePersistProcessing(Study instance) {

        instance = doArchive( instance ) ;

        if (instance.getTopicGroups() != null & !instance.isArchived())
            instance.getTopicGroups().forEach(c->{
                c.setChangeKind(AbstractEntityAudit.ChangeKind.UPDATED_PARENT);
                c.setChangeComment("");
            });
        return instance;
    }




    private Study postLoadProcessing(Study instance) {
        if (StackTraceFilter.stackContains("getPdf","getXml")) {
            LOG.debug("PDF -> fetching  concepts ");
            Hibernate.initialize(instance.getTopicGroups());
            instance.getTopicGroups().forEach( c-> Hibernate.initialize(c.getConcepts() ));

            instance.getTopicGroups().forEach(t-> {
                t.getTopicQuestionItems().forEach( qiLoader::fill );
                t.getConcepts().forEach( c-> {
                    c.getConceptQuestionItems().forEach( qiLoader::fill  );
                    c.getChildren().forEach(
                       cc-> cc.getConceptQuestionItems().forEach( qiLoader::fill  )
                    );
                } );
            });

            // when we print hierarchy we don't need qi concept reference....
        }
        return instance;
    }
}
