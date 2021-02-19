package no.nsd.qddt.domain;


import no.nsd.qddt.domain.user.User;
import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
@Entity(name = "revinfo")
//@RevisionEntity( RevisionEntityListenerImpl.class )
public class RevisionEntityImpl extends DefaultRevisionEntity {

    @ManyToOne
    @JoinColumn( nullable = false)
    @LastModifiedBy
    User modifiedBy;

    @LastModifiedDate
    Timestamp modified;


    public UserJson getModifiedBy() {
        return  new UserJson(modifiedBy);
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}



