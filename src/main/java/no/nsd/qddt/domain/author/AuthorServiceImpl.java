package no.nsd.qddt.domain.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public long count() {
        return authorRepository.count();
    }

    @Override
    public boolean exists(UUID id) {
        return authorRepository.existsById(id);
    }

    @Override
    public Author findOne(UUID id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EDITOR')")
    public Author save(Author instance) {
        return authorRepository.save(instance);
    }

    @Override
    public void delete(UUID id) {
        authorRepository.deleteById(id);
    }

    protected Author prePersistProcessing(Author instance) {
        return instance;
    }


    protected Author postLoadProcessing(Author instance) {
        return instance;
    }

    @Override
    public Page<Author> findAllPageable(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Page<Author> findbyPageable(String name, String about, String email, Pageable pageable) {
        return authorRepository.findAuthorsByAboutContainingOrNameContainingOrEmailContaining( about,name, email, pageable);
    }
}
