package no.nsd.qddt.domain.publicationstatus;

import no.nsd.qddt.domain.BaseRepository;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface PublicationStatusRepository extends BaseRepository<PublicationStatus,Long> {

    List<PublicationStatus> findAllByAgencyOrderByStatus(Agency agency);
}