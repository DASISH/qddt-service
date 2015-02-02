package no.nsd.qddt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Stig Norland
 */
public interface AbstractService<T> {

    public T findById(Long id);

    public List<T> findAll();

    public Page<T> findAllPageable(Pageable pageable);

    public T save(T instance);

    public  void delete(T instance);

}
