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

    @Type(type="pg-uuid")
    @Column(name="controlconstruct_ref_id")
    private UUID controlConstructRefId;

    @Column(name = "controlconstruct_ref_revision")
    private Long controlConstructRevision;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "controlconstruct_ref_id",updatable = false,insertable = false)
//    private ControlConstruct controlConstructRefInternal;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getControlConstructRefId() {
        return controlConstructRefId;
    }

    public void setControlConstructRefId(UUID controlConstructRefId) {
        this.controlConstructRefId = controlConstructRefId;
    }

    public Long getControlConstructRevision() {
        return controlConstructRevision;
    }

    public void setControlConstructRevision(Long controlConstructRevision) {
        this.controlConstructRevision = controlConstructRevision;
    }

//    public ControlConstruct getControlConstructRefInternal() {
//        return controlConstructRefInternal;
//    }
//
//    public void setControlConstructRefInternal(ControlConstruct controlConstructRefInternal) {
//        this.controlConstructRefInternal = controlConstructRefInternal;
//    }

    @Override
    public String toString() {
        return String.format(
                "FlowParameter (name=%s, ref=%s)", this.name, null);
    }


}
