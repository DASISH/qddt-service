package domain;

import no.nsd.qddt.QDDT;
import no.nsd.qddt.domain.BaseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Default test class that can be implemented by all
 * test classes testing {@link org.springframework.stereotype.Service} classes.
 *
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public abstract class AbstractServiceTest {

    private BaseService baseService;

    public void setService(BaseService baseService) {
        this.baseService = baseService;
    }

    @Test
    public void testCount() throws Exception {
        long count = baseService.count();
        System.out.println(count);
        assertThat("Should be three", baseService.count(), is(3L));
    }

    @Test
    public void testExists() throws Exception {
//        baseService
    }

    @Test
    public void testFindOne() throws Exception {

    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testSaveAll() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testDeleteAll() throws Exception {

    }
}