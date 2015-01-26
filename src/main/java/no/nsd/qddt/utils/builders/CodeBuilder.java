package no.nsd.qddt.utils.builders;


import no.nsd.qddt.domain.response.Code;

/**
 * @author Stig Norland
 */
public class CodeBuilder {
    private String category;
    private String value;
    private String tag;

    public CodeBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public CodeBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public CodeBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public Code createCode() {
        Code code = new Code();
        code.setName(this.tag);
        code.setCategory(this.category);
        code.setCodeValue(this.value);

        return ;
    }

}
