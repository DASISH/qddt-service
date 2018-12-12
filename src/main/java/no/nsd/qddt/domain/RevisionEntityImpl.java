package no.nsd.qddt.domain;


import no.nsd.qddt.domain.user.User;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Stig Norland
 */
@Entity
@RevisionEntity( RevisionEntityListenerImpl.class )
public class RevisionEntityImpl extends DefaultRevisionEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User modifiedBy;

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}



