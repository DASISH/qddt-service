package no.nsd.qddt.service;

import no.nsd.qddt.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface StudyService {

   public Study findOne(Long id);

   public List<Study> findAll();

   public Study save(Study study);

   public Revision<Integer, Study> findLastChange(Long id);

   public Page<Revision<Integer, Study>> findAllRevisionsPageable(Long id,Pageable pageable);
}
