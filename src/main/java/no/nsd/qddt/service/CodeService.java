package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;
import java.util.Optional;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService extends AbstractServiceAudit<Code>{

    public List<Code> findByHashTag(String tag);
}
