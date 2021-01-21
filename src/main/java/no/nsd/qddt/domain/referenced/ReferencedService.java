package no.nsd.qddt.domain.referenced;

import no.nsd.qddt.domain.classes.interfaces.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface ReferencedService  extends BaseService<Referenced, UUID>{
    Page findAll( String kind, Pageable pageable);
}
