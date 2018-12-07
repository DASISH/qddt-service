package no.nsd.qddt.configuration;


import no.nsd.qddt.domain.user.User;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

/**
 * @author Stig Norland
 */
@Entity
@RevisionEntity( RevisionEntityListenerImpl.class )
public class RevisionEntityImpl extends DefaultRevisionEntity {

    User modifiedBy;

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}



