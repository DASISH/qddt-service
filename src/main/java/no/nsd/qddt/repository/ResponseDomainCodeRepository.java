package no.nsd.qddt.repository;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import no.nsd.qddt.domain.response.ResponseDomainCodeId;
import org.springframework.data.history.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainCodeRepository  extends RevisionRepository<ResponseDomainCode, ResponseDomainCodeId, Integer>, JpaRepository<ResponseDomainCode, ResponseDomainCodeId> {

    /**
     * Find a {@link no.nsd.qddt.domain.response.ResponseDomainCode} by using its
     * {@link no.nsd.qddt.domain.response.ResponseDomainCodeId} made up of one
     * {@link no.nsd.qddt.domain.response.Code} and one {@link no.nsd.qddt.domain.response.ResponseDomain}.
     * @param responseDomainCodeId to query
     * @return the requested entity
     */
    ResponseDomainCode findByPk(ResponseDomainCodeId responseDomainCodeId);

}