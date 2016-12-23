package no.nsd.qddt.domain.category;


import java.util.List;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface CategoryJson {

    UUID getId();
    String getLabel();
    String getName();
//    CodeJson getCode();
    List<CategoryJson> getChildren();
}
