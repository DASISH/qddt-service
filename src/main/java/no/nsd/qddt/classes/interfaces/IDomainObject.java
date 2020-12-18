package no.nsd.qddt.classes.interfaces;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.security.user.json.UserJson;

import java.sql.Timestamp;

/**
 * @author Stig Norland
 */
public interface IDomainObject extends IWebMenuPreview, IXmlBuilder {

    UserJson getModifiedBy();
    Timestamp getModified();
    Agency getAgency();
    String getClassKind();

}
