package no.nsd.qddt.service;

import no.nsd.qddt.domain.HashTag;
import no.nsd.qddt.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Stig Norland
 */

@Service("tagService")
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository)
    {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HashTag> findAll() {

        return tagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HashTag> findAll(Pageable pageable) {

        return tagRepository.findAll(pageable);
    }
}
