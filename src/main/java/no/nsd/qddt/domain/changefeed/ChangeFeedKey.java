package no.nsd.qddt.domain.changefeed;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Stig Norland
 */
@Embeddable
public class ChangeFeedKey implements Serializable {
    private static final long serialVersionUID = -7581799247841869060L;

    @Column(name = "ref_id")
    protected UUID refId;

    @Column(name = "ref_rev")
    protected Integer refRev;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int2")
    protected ActionKind refAction;

    public ChangeFeedKey() {}

    public ChangeFeedKey(UUID refId, Integer refRev) {
        this.refId = refId;
        this.refRev = refRev;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeFeedKey that = (ChangeFeedKey) o;

        if (!refId.equals( that.refId )) return false;
        return refRev.equals( that.refRev );
    }

    @Override
    public int hashCode() {
        int result = refId.hashCode();
        result = 31 * result + refRev.hashCode();
        return result;
    }
}

