package no.nsd.qddt.service;

import no.nsd.qddt.domain.response.Code;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;
import java.util.Optional;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CodeService {

    Code findById(Long id);

    List<Code> findByHashTag(String tag);

    List<Code> findAll();

    Code save(Code code);

    public Revision<Integer, Code> findLastChange(Long id);

    public Page<Revision<Integer, Code>> findAllRevisionsPageable(Code study, int min, int max);

    public List<String> findAllHashTags();

}
