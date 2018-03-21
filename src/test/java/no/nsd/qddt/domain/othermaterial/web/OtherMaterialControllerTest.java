package no.nsd.qddt.domain.othermaterial.web;

import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.othermaterial.pojo.OtherMaterial;
import no.nsd.qddt.domain.othermaterial.OtherMaterialService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author Dag Heradstveit
 */
public class OtherMaterialControllerTest  extends ControllerWebIntegrationTest {

    @Autowired
    private OtherMaterialService entityService;

    private OtherMaterial entity;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        entity = new OtherMaterial();
        entity.setFileName("A test entity");
        entity = entityService.save(entity);

        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/othermaterial/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        entity.setFileName(entity.getFileName() + " edited");

        mvc.perform(post("/othermaterial").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(entity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(entity.getFileName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        OtherMaterial aEntity = new OtherMaterial();
        aEntity.setFileName("Posted entity");

        mvc.perform(post("/othermaterial/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aEntity)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aEntity.getFileName())))
                .andExpect(jsonPath("$.changeKind", is(AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/othermaterial/delete/"+entity.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instruction should no longer exist", entityService.exists(entity.getId()));
    }

    @Test
    public void testBinaryFileDownload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "\1\2\3\4\5".getBytes());

         entityService.saveFile(file,entity.getId(),"CC");


        mvc.perform(get("/othermaterial/files/{id}",entity.getId()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(new byte[]{'\1', '\2', '\3', '\4', '\5'}))
                .andExpect(status().isOk());
    }


    @Test
    public void testFileUp() throws IOException {
    }
}