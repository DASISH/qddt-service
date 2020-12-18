package no.nsd.qddt.domain.publicationstatus;

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
        return repository.existsById(id);
    }

    @Override
    public PublicationStatus findOne(Long id) {
        return repository.findById(id).get();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public PublicationStatus save(PublicationStatus instance) {
        return repository.save(
                prePersistProcessing(instance));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public void delete(Long id) {
        repository.deleteById(id);
    }



    private PublicationStatus prePersistProcessing(PublicationStatus instance) {
        return instance;
    }


    protected PublicationStatus postLoadProcessing(PublicationStatus instance) {

        return instance;
    }

    @Override
    public List<PublicationStatus> findAll() {
        List<PublicationStatus> retvals = repository.findAllByParentIdIsNullOrderByParentIdx();
        // er denne fremdeles nødvendig?
        retvals.forEach(c -> c.getChildren().size());
        return retvals;
    }
}
