package no.nsd.qddt.domain.othermaterial;

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
public class OtherMaterialServiceTest  extends AbstractServiceTest {

    @Autowired
    private OtherMaterialService otherMaterialService;

    @Autowired
    private OtherMaterialRepository otherMaterialRepository;

    @Before
    public void setup() {
        super.setup();
        super.setBaseRepositories(otherMaterialRepository);
    }

    @Test
    @Override
    public void testCount() throws Exception {
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial One");
        otherMaterialService.save(otherMaterial);

        otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial Two");
        otherMaterialService.save(otherMaterial);

        otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial Three");
        otherMaterialService.save(otherMaterial);

        assertThat("Should be three", otherMaterialService.count(), is(3L));
    }

    @Test
    @Override
    public void testExists() throws Exception {
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Existing otherMaterial");
        otherMaterial = otherMaterialService.save(otherMaterial);
        assertTrue("OtherMaterial should exist", otherMaterialService.exists(otherMaterial.getId()));
    }

    @Test
    @Override
    public void testFindOne() throws Exception {
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Existing otherMaterial");
        otherMaterial = otherMaterialService.save(otherMaterial);
        assertNotNull("OtherMaterial should not be null", otherMaterialService.findOne(otherMaterial.getId()));
    }

    @Test
    @Override
    public void testSave() throws Exception {
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Existing otherMaterial");
        assertNotNull("OtherMaterial should be saved", otherMaterialService.save(otherMaterial));
    }

    @Test
    @Override
    public void testSaveAll() throws Exception {
        List<OtherMaterial> agencyList = new ArrayList<>();
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial One");
        agencyList.add(otherMaterial);

        otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial Two");
        agencyList.add(otherMaterial);

        otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial Three");
        agencyList.add(otherMaterial);

        otherMaterialService.save(agencyList);

        assertEquals("Should return 3", otherMaterialService.count(), 3L);
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDelete() throws Exception {
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Existing otherMaterial");
        otherMaterial = otherMaterialService.save(otherMaterial);
        otherMaterialService.delete(otherMaterial.getId());

        assertNull("Should return null", otherMaterialService.findOne(otherMaterial.getId()));
    }

    @Test(expected = ResourceNotFoundException.class)
    @Override
    public void testDeleteAll() throws Exception {
        List<OtherMaterial> agencyList = new ArrayList<>();
        OtherMaterial otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial One");
        agencyList.add(otherMaterial);

        otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial Two");
        agencyList.add(otherMaterial);

        otherMaterial = new OtherMaterial();
        otherMaterial.setFileName("Test OtherMaterial Three");
        agencyList.add(otherMaterial);

        agencyList = otherMaterialService.save(agencyList);
        otherMaterialService.delete(agencyList);

        agencyList.forEach(a -> assertNull("Should return null", otherMaterialService.findOne(a.getId())));

    }
}
