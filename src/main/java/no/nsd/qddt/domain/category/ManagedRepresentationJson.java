package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.HierarchyLevel;
import no.nsd.qddt.domain.embedded.ResponseCardinality;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface ManagedRepresentationJson {

    UUID getId();
    String getName();
    String getLabel();
    String getDescription();
    List<CategoryJson> getChildren();
    ResponseCardinality getInputLimit();
    CategoryRelationCodeType getClassificationLevel();
    HierarchyLevel getHierarchyLevel();
    CategoryType getCategoryType();

}
