package no.nsd.qddt.domain.Category;

import no.nsd.qddt.domain.Category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stig Norland
 */
public interface CategoryService {

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

}
