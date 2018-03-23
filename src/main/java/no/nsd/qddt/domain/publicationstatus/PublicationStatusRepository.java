package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.agency.Agency;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationStatusRepository extends BaseRepository<PublicationStatus,Long> {

    List<PublicationStatus> findAllByAgencyAndParentIdIsNullOrderByParentIdx(Agency agency);

    List<PublicationStatus> findAllByAgencyAndParentIdIsNull(Agency agency,Sort sort);
}
