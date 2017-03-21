package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.utils.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public boolean exists(Long id) {
        return repository.exists(id);
    }

    @Override
    public PublicationStatus findOne(Long id) {
        return repository.findOne(id);
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
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public void delete(List<PublicationStatus> instances) {
        repository.delete(instances);
    }


    @Override
    public List<PublicationStatusJsonListView> findAll() {
        Agency agency = SecurityContext.getUserDetails().getUser().getAgency();
        return repository.findAllByAgencyOrderByStatus(agency).stream()
                .map(s->new PublicationStatusJsonListView(s))
                .collect(Collectors.toList());
    }
}
