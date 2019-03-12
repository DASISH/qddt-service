package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stig Norland
 */
@Service("publicationStatusService")
public class PublicationStatusServiceImpl implements PublicationStatusService {

    private final PublicationStatusRepository repository;

    @Autowired
    public PublicationStatusServiceImpl(PublicationStatusRepository publicationStatusRepository){
        this.repository = publicationStatusRepository;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(Long id) {
        return repository.exists(id);
    }

    @Override
    public PublicationStatus findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public PublicationStatus save(PublicationStatus instance) {
        return repository.save(
                prePersistProcessing(instance));
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
//    public List<PublicationStatus> save(List<PublicationStatus> instances) {
//        instances.forEach(this::prePersistProcessing);
//        return repository.save(instances);
//    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(List<PublicationStatus> instances) {
        repository.delete(instances);
    }


    private PublicationStatus prePersistProcessing(PublicationStatus instance) {
        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        instance.setAgency(agency);
        return instance;
    }


    protected PublicationStatus postLoadProcessing(PublicationStatus instance) {

        return instance;
    }


    @Override
    public List<PublicationStatus> findAll() {
//        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        List<PublicationStatus> retvals = repository.findAllByAgencyAndParentIdIsNullOrderByParentIdx(null);
        retvals.forEach(c -> c.getChildren().size());
        return retvals;
    }
}
