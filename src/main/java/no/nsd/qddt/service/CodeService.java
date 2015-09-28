package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.Code;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService extends BaseServiceAudit<Code,UUID> {

    public List<Code> findByHashTag(String tag);
}
