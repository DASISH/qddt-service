package no.nsd.qddt.domain.publicationstatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class PublicationStatusJsonListView {

    Long id;
    String status;
    List<PublicationStatusJsonListView> children = new ArrayList<>();

    PublicationStatusJsonListView(PublicationStatus publicationStatus){
        id = publicationStatus.getId();
        status = publicationStatus.getStatus();
        children = publicationStatus.getChildren().stream()
                .map(c->new PublicationStatusJsonListView(c))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public List<PublicationStatusJsonListView> getChildren() {
        return children;
    }
}
