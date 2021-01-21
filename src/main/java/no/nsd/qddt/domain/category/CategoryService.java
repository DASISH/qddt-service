package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Dag Østgulen Heradstveit
 */
public interface CategoryService extends BaseService<Category, UUID> {

    Page<Category>findBy(String hierarchyLevel, String categoryType,String name, String description, String xmlLang, Pageable pageable);

}
