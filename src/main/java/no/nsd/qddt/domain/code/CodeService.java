package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService extends BaseService<Code, UUID> {

    public List<Code> findByHashTag(String tag);
}
