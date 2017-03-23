package no.nsd.qddt.domain.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public long count() {
        return authorRepository.count();
    }

    @Override
    public boolean exists(UUID uuid) {
        return authorRepository.exists(uuid);
    }

    @Override
    public Author findOne(UUID uuid) {
        return authorRepository.findOne(uuid);
    }

    @Override
    public Author save(Author instance) {
        return authorRepository.save(instance);
    }

    @Override
    public List<Author> save(List<Author> instances) {
        return authorRepository.save(instances);
    }

    @Override
    public void delete(UUID uuid) {
        authorRepository.delete(uuid);
    }

    @Override
    public void delete(List<Author> instances) {
        authorRepository.delete(instances);
    }

    @Override
    public Author prePersistProcessing(Author instance) {
        return instance;
    }

    @Override
    public Author postLoadProcessing(Author instance) {
        return instance;
    }

    @Override
    public Page<Author> findAllPageable(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }
}
