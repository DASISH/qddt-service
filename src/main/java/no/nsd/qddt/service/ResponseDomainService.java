package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface ResponseDomainService {

    public List<ResponseDomainCode> findAll();
}
