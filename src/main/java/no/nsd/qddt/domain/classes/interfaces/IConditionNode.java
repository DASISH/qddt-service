package no.nsd.qddt.domain.classes.interfaces;

import no.nsd.qddt.domain.controlconstruct.pojo.ConditionKind;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IConditionNode {
    UUID getId();

    Version getVersion();

    String getName() ;

    ConditionKind getConditionKind();

    String getClassKind() ;

    String getCondition() ;
}
