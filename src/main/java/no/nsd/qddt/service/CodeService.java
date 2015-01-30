package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.Code;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService extends AbstractServiceAudit<Code>{

    public List<Code> findByHashTag(String tag);
}
