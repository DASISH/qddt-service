package no.nsd.qddt.domain.agency.web;


import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.agency.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Dag Heradstveit
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AgencyControllerTest  {

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
