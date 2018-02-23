package no.nsd.qddt.domain.controlconstructparameter;


import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * Parameter is a bad word to use, so we have changed it internally to ResponseReference
 * as a parameter read response(s) and the current CC uses this response
 * if a Question text has a [A_NAME] in it, QDDT will replace this text with the response
 * of a ResponseReference with name A_NAME.
 *
 * @author Stig Norland
 */


@Audited
@Embeddable
public class ResponseReference implements java.io.Serializable {

    private static final long serialVersionUID = -7261847349839337877L;

    private String name;
    private UUID controlConstructRefId;
    private Long controlConstructRevision;



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }



    @Type(type="pg-uuid")
    @Column(name="controlconstruct_ref_id")
    public UUID getControlConstructRefId() {
        return controlConstructRefId;
    }
    public void setControlConstructRefId(UUID controlConstructRefId) {
        this.controlConstructRefId = controlConstructRefId;
    }



    @Column(name = "controlconstruct_ref_revision")
    public Long getControlConstructRevision() {
        return controlConstructRevision;
    }
    public void setControlConstructRevision(Long controlConstructRevision) {
        this.controlConstructRevision = controlConstructRevision;
    }



    @Override
    public String toString() {
        return String.format(
                "FlowParameter (name=%s, ref=%s)", this.name, null);
    }


}
