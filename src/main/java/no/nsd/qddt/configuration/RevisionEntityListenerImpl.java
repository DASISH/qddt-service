package no.nsd.qddt.configuration;

import no.nsd.qddt.utils.SecurityContext;
import org.hibernate.envers.RevisionListener;

/**
 * @author Stig Norland
 */
public class RevisionEntityListenerImpl implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        ( (RevisionEntityImpl) revisionEntity ).setModifiedBy( SecurityContext.getUserDetails().getUser() );
    }
}
