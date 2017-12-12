package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PublicationStatus save(PublicationStatus instance) {
        return repository.save(
                prePersistProcessing(instance));
    }

    @Override
    public List<PublicationStatus> save(List<PublicationStatus> instances) {
        instances.forEach(this::prePersistProcessing);
        return repository.save(instances);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
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
        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        return repository.findAllByAgencyAndParentIdIsNull(agency);
    }
}
