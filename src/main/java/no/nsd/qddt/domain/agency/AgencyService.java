package no.nsd.qddt.domain.agency;

import no.nsd.qddt.domain.BaseService;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface AgencyService extends BaseService<Agency, UUID> {

    List<Agency> getAll();

    Page<Agency> findByNamePageable(String name, Pageable pageable);
}
