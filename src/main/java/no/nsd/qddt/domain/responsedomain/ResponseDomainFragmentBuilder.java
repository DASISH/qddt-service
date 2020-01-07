package no.nsd.qddt.domain.responsedomain;

import no.nsd.qddt.domain.AbstractEntity;
import no.nsd.qddt.domain.category.Category;
import no.nsd.qddt.domain.xml.XmlDDIFragmentBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ResponseDomainFragmentBuilder extends XmlDDIFragmentBuilder<ResponseDomain> {

    private Map<UUID,Category> rdContent;
    private final String xmlDomain =
        "<d:%1$s>\n" +
            "\t%2$s" +
            "\t%3$s" +
        "</d:%1$s>\n";


    public ResponseDomainFragmentBuilder(ResponseDomain responseDomain) {
        super(responseDomain);
        rdContent = responseDomain.getFlatManagedRepresentation( responseDomain.getManagedRepresentation() ).stream()
            .collect( Collectors.toMap( AbstractEntity::getId, c->c ));

    }

    @Override
    public void setEntityBody(Map<UUID, String> fragments) {
        fragments.putIfAbsent( entity.getId(), getXmlBody() );
//        rdContent.setEntityBody( fragments );
    }

    private String getXmlBody() {
        return String.format( xmlDomain,
            entity.getResponseKind().getDDIName(),
            getId()+ getRationale()+ getBasedOn(),
            ""
            );
    }


}
