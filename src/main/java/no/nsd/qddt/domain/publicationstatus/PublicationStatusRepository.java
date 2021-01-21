package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.classes.interfaces.BaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationStatusRepository extends BaseRepository<PublicationStatus,Long> {

    List<PublicationStatus> findAllByParentIdIsNullOrderByParentIdx();

    List<PublicationStatus> findAllByParentIdIsNull(Sort sort);
}
