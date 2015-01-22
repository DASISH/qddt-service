package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainRepository  extends RevisionRepository<ResponseDomain, Long, Integer>, JpaRepository<ResponseDomain, Long> {
}
