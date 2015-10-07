package no.nsd.qddt.domain;

import com.google.common.collect.Lists;
import no.nsd.qddt.QDDT;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Default test class that can be implemented by all
 * test classes testing {@link org.springframework.stereotype.Service} classes.
 *
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public abstract class AbstractServiceTest implements ServiceTest {

    private List<BaseRepository> baseRepository;

    public void setBaseRepositories(BaseRepository...baseRepository) {
        this.baseRepository =  Arrays.asList(baseRepository);
    }

    @After
    public void tearDown() {
        baseRepository.forEach(CrudRepository::deleteAll);
    }
}