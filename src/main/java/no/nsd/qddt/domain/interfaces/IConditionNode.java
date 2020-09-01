package no.nsd.qddt.domain.interfaces;

import no.nsd.qddt.domain.controlconstruct.pojo.ConditionKind;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface IConditionNode {
    public UUID getId();

    public Version getVersion();

    public String getName() ;

    public ConditionKind getConditionKind();

    public String getClassKind() ;

    public String getCondition() ;
}
