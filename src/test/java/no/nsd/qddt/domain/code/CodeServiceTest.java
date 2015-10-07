package no.nsd.qddt.domain.code;

import no.nsd.qddt.domain.AbstractServiceTest;
import no.nsd.qddt.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Dag Østgulen Heradstveit.
 */
public class CodeServiceTest extends AbstractServiceTest {
    
    @Autowired
    private CodeService codeService;
    
    @Autowired    
    private CodeRepository codeRepository;
    
    @Before
    public void setup() {
        super.setBaseRepositories(codeRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        Code code = new Code();
        code.setName("Test Code One");
        codeService.save(code);

        code = new Code();
        code.setName("Test Code Two");
        codeService.save(code);

        code = new Code();
        code.setName("Test Code Three");
        codeService.save(code);

        assertThat("Should be three", codeService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        Code code = new Code();
        code.setName("Existing code");
        code = codeService.save(code);
        assertTrue("Code should exist", codeService.exists(code.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        Code code = new Code();
        code.setName("Existing code");
        code = codeService.save(code);
        assertNotNull("Code should not be null", codeService.findOne(code.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        Code code = new Code();
        code.setName("Existing code");
        assertNotNull("Code should be saved", codeService.save(code));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<Code> agencyList = new ArrayList<>();
        Code code = new Code();
        code.setName("Test Code One");
        agencyList.add(code);

        code = new Code();
        code.setName("Test Code Two");
        agencyList.add(code);

        code = new Code();
        code.setName("Test Code Three");
        agencyList.add(code);

        codeService.save(agencyList);

        assertEquals("Should return 3", codeService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        Code code = new Code();
        code.setName("Existing code");
        code = codeService.save(code);
        codeService.delete(code.getId());

        assertNull("Should return null", codeService.findOne(code.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<Code> agencyList = new ArrayList<>();
        Code code = new Code();
        code.setName("Test Code One");
        agencyList.add(code);

        code = new Code();
        code.setName("Test Code Two");
        agencyList.add(code);

        code = new Code();
        code.setName("Test Code Three");
        agencyList.add(code);

        agencyList = codeService.save(agencyList);
        codeService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", codeService.findOne(a.getId())));

    }
}
