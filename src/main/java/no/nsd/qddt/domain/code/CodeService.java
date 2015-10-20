package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService extends BaseService<Code, UUID> {

    public List<Code> findByHashTag(String tag);

    public Page<Code>findByHashTagPageable(String tag, Pageable pageable);

//    public List<String> findAllCategoies();
//
//    public Page<String> findAllCategoies(Pageable pageable);
}
