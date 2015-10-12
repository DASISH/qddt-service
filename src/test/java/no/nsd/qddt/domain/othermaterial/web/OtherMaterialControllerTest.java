package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.othermaterial.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dag Heradstveit
 */
public class OtherMaterialControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private OtherMaterialService otherMaterialService;

    private OtherMaterial otherMaterial;

    @Override
    public void setup() {
        super.setup();

        otherMaterial = new OtherMaterial();
        otherMaterial.setDescription("A test material");
        otherMaterial.setPath("(/material/path");
        otherMaterial = otherMaterialService.save(otherMaterial);
    }

    @Test
    public void findByIdFailNotFoundTest() throws Exception {
        mvc.perform(get("/othermaterial/"+otherMaterial.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}
