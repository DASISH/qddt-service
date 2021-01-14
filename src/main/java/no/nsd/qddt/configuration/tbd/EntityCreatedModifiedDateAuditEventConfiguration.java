//package no.nsd.qddt.configuration;
//
//import no.nsd.qddt.classes.AbstractEntity;
//import no.nsd.qddt.classes.AbstractEntityAudit;
//import no.nsd.qddt.domain.user.User;
//import no.nsd.qddt.configuration.tbd.SecurityContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Configurable;
//
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//
//
///**
// * Creates and updates entities based on global rules
// * of implementations of {@link AbstractEntity} and {@link AbstractEntityAudit}.
// *
// * Be aware that this class will cause {@link NullPointerException} if BeforeSecurityContext is not set in tests.
// *
// * @author Dag Østgulen Heradstveit
// * @author Stig Norland
// */
//@Configurable
//public class EntityCreatedModifiedDateAuditEventConfiguration {
//    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * Run before persisting a new entity.
//     * @param entity target for persistence
//     */
//    @SuppressWarnings("UnusedAssignment")
//    @PrePersist
//    @PreUpdate
//    public void createOrUpdate(AbstractEntity entity) {
//        try {
//            User user = SecurityContext.getUserDetails().getUser();
//            if (entity.getModifiedBy() == null) {
//                entity.setModifiedBy( user );
//            }
//            if (entity instanceof AbstractEntityAudit) {
//                ((AbstractEntityAudit) entity).setAgency( user.getAgency() );
//                LOG.info( "AbstractEntityAudit EventConfiguration CreateOrUpdate done " + entity.getClass().getSimpleName() );
//            }
//            else
//                LOG.info("Entity EventConfiguration CreateOrUpdate  OTHER "+ entity.getClass().getSimpleName());
//
//        } catch (Exception e){
//            LOG.error("Entity EventConfiguration", e);
//        }
//    }
//
//}
