package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("publicationStatusService")
public class PublicationStatusServiceImpl implements PublicationStatusService {

    private PublicationStatusRepository repository;

    @Autowired
    public PublicationStatusServiceImpl(PublicationStatusRepository publicationStatusRepository){
        this.repository = publicationStatusRepository;

    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return repository.exists(uuid);
    }

    @Override
    public PublicationStatus findOne(UUID uuid) {
        return repository.findOne(uuid);
    }

    @Override
    public <S extends PublicationStatus> S save(S instance) {
        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        instance.setAgency(agency);
        return repository.save(instance);
    }

    @Override
    public List<PublicationStatus> save(List<PublicationStatus> instances) {
        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        instances.forEach(i->i.setAgency(agency));
        return repository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        repository.delete(uuid);
    }

    @Override
    public void delete(List<PublicationStatus> instances) {
        repository.delete(instances);
    }


    @Override
    public List<PublicationStatus> findAll() {
        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        return repository.findAllByAgencyOrderByStatus(agency);
    }
}
