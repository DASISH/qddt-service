package no.nsd.qddt.domain.role;

import no.nsd.qddt.classes.interfaces.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */

public interface AuthorityService extends BaseService<Authority,UUID> {
    List<Authority> findAll();
}
