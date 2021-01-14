package no.nsd.qddt.classes;

import no.nsd.qddt.configuration.tbd.SecurityContext;
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
