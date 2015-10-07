package no.nsd.qddt.domain;

import no.nsd.qddt.QDDT;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Default test class that can be implemented by all
 * test classes testing {@link org.springframework.stereotype.Service} classes.
 *
 * Created by Dag Østgulen Heradstveit.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QDDT.class)
public abstract class AbstractServiceTest {

}