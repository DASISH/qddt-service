package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface ResponseDomainCodeService extends BaseServiceAudit<ResponseDomainCode> {

    public List<ResponseDomainCode> findByResponseDomainId(Long responseDomainId);

    public List<ResponseDomainCode> findByCodeId(Long codeId);

}