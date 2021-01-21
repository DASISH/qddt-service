package no.nsd.qddt.domain.instrument.web;

import no.nsd.qddt.domain.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.ControllerWebIntegrationTest;
import no.nsd.qddt.domain.instrument.pojo.Instrument;
import no.nsd.qddt.domain.instrument.InstrumentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Stig Norland
 */
public class InstrumentControllerTest  extends ControllerWebIntegrationTest {

    @Autowired
    private InstrumentService instrumentService;

    private Instrument instrument;

    @Override
    public void setup() {
        super.setup();

        super.getBeforeSecurityContext().createSecurityContext();

        instrument = new Instrument(  );
        instrument.setName("A test instrument");
        instrument = instrumentService.save(instrument);

        super.getBeforeSecurityContext().destroySecurityContext();

    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/instrument/"+instrument.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        instrument.setName(instrument.getName() + " edited");

        mvc.perform(post("/instrument").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(instrument)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(instrument.getName())))
                .andExpect(jsonPath("$.changeKind", is( AbstractEntityAudit.ChangeKind.IN_DEVELOPMENT.toString())))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        Instrument aInstrument = new Instrument(  );
        aInstrument.setName("Posted instrument");

        mvc.perform(post("/instrument/create").header("Authorization", "Bearer " + accessToken)
                .contentType(rest.getContentType())
                .content(rest.json(aInstrument)))
                .andExpect(content().contentType(rest.getContentType()))
                .andExpect(jsonPath("$.name", is(aInstrument.getName())))
                .andExpect(jsonPath("$.changeKind", is( AbstractEntityAudit.ChangeKind.CREATED.toString())))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/instrument/delete/"+instrument.getId()).header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertFalse("Instrument should no longer exist", instrumentService.exists(instrument.getId()));
    }
}
