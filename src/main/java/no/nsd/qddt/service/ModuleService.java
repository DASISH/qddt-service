package no.nsd.qddt.service;

import no.nsd.qddt.domain.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revision;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface  ModuleService {

    public Module findById(Long id);

    public List<Module> findAll();

    public Module save(Module module);

    public Revision<Integer, Module> findLastChange(Long id);

    public Page<Revision<Integer, Module>> findAllRevisionsPageable(Module module , int min, int max);

}
