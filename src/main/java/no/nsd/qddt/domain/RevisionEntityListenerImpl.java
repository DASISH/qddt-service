package no.nsd.qddt.domain;

import no.nsd.qddt.security.SecurityContext;
import org.hibernate.envers.RevisionListener;

/**
 * @author Stig Norland
 */
public class RevisionEntityListenerImpl implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
//        System.err.println("setModifiedBy");
        ( (RevisionEntityImpl) revisionEntity ).setModifiedBy( SecurityContext.getUserDetails().getUser() );
    }
}
