package no.nsd.qddt.domain.publication.audit;

import no.nsd.qddt.domain.publication.Publication;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 **/

@Repository
interface PublicationAuditRepository extends RevisionRepository<Publication, UUID, Integer> {

}

