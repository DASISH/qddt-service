package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.ResponseDomainCode;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 */
public interface ResponseDomainCodeService extends AbstractService<ResponseDomainCode> {

    public List<ResponseDomainCode> findByResponseDomainId(Long responseDomainId);

    public List<ResponseDomainCode> findByCodeId(Long codeId);

}