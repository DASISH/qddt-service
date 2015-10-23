package no.nsd.qddt.domain.bcategory;

import no.nsd.qddt.domain.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryService extends BaseService<Category, UUID> {

    List<Category>findByTag(String name);

    Page<Category>findByTagPageable(String name, Pageable pageable);

//    public List<String> findAllCategoies();
//
//    public Page<String> findAllCategoies(Pageable pageable);
}
