package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class ResponseDomainFragmentBuilder extends XmlDDIFragmentBuilder<ResponseDomain> {
    public ResponseDomainFragmentBuilder(ResponseDomain responseDomain) {
        super(responseDomain);
    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {

    }
}
