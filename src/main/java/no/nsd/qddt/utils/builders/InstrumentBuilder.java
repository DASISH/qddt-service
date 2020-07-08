package no.nsd.qddt.utils.builders;

import no.nsd.qddt.domain.instrument.pojo.Instrument;

public class InstrumentBuilder {

    private String changeComment;
    private String name;

    public InstrumentBuilder setChangeComment(String changeComment) {
        this.changeComment = changeComment;
        return this;
    }

    public InstrumentBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public Instrument createInstrument() {
        Instrument instrument = new Instrument(  );
        instrument.setName(this.name);
        instrument.setChangeComment(this.changeComment);

        return instrument;
    }
}
