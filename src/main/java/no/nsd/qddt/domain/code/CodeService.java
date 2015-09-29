package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.BaseServiceAudit;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService extends BaseServiceAudit<Code,UUID> {

    public List<Code> findByHashTag(String tag);
}
