package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainCodeRepository  extends RevisionRepository<ResponseDomainCode, Long, Integer>, JpaRepository<ResponseDomainCode, Long> {
}
