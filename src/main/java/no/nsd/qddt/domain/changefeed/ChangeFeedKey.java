package no.nsd.qddt.domain.changefeed;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
        return Objects.equal( refId, that.refId ) &&
            Objects.equal( refRev, that.refRev );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( refId, refRev );
    }
}

