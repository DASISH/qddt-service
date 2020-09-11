package no.nsd.qddt.domain.interfaces;

import no.nsd.qddt.domain.agency.Agency;
import no.nsd.qddt.domain.user.json.UserJson;

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
