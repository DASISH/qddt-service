package no.nsd.qddt.domain.agency.web;

import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.agency.AgencyService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dag Heradstveit
 */
public class AgencyControllerTest extends ControllerWebIntegrationTest {

    @Autowired
    private AgencyService agencyService;

    private Agency agency;

    @Override
    public void setup() {
        super.setup();
        super.getBeforeSecurityContext().createSecurityContext();
        agency = new Agency();
        agency.setName("Test agency");
        agency = agencyService.save(agency);
        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/agency/"+agency.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        agency.setName(agency.getName() + " edited");

        mvc.perform(post("/agency").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(agency)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(agency.getName())))
                .andExpect(status().isOk())
                .andDo(document("update",
                        responseFields(
                                fieldWithPath("id").description(""),
                                fieldWithPath("name").description(""),
                                fieldWithPath("modified").description(""),
                                fieldWithPath("modifiedBy").description("")
                                )
                ));
    }

    @Test
    public void create() throws Exception {
        Agency aAgency = new Agency();
        aAgency.setName("Posted agency");

        mvc.perform(post("/agency/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aAgency))
                .accept(rest.getContentType()))
                .andExpect(status().isCreated());
    }
}
