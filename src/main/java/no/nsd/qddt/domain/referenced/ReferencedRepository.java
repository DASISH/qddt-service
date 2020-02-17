package no.nsd.qddt.domain.referenced;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Repository
public interface ReferencedRepository extends JpaRepository<Referenced, UUID> {}
