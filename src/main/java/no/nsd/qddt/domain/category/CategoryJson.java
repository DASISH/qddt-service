package no.nsd.qddt.domain.category;

import no.nsd.qddt.domain.code.CodeJson;

import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface CategoryJson {

    UUID getId();
    String getLabel();
    String getName();
    CodeJson getCode();
    List<CategoryJson> getChildren();
}
