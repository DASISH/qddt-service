package no.nsd.qddt.domain.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */


@Service("authorityService")
class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }


    @Override
    public long count() {
        return authorityRepository.count();
    }

    @Override
    public boolean exists(UUID id) {
        return authorityRepository.existsById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Authority findOne(UUID id) {
        return authorityRepository.findById( id ).get();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Authority save(Authority instance) {
        return postLoadProcessing(
                authorityRepository.save(
                        prePersistProcessing(instance)));
    }


    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(UUID id) {
        authorityRepository.deleteById(id);
    }

    private Authority prePersistProcessing(Authority instance) {
        return instance;
    }

    private Authority postLoadProcessing(Authority instance) {
        return instance;
    }

}
