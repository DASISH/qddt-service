package no.nsd.qddt.domain.interfaces;

public interface IParentRef extends IEntityRef {

    String getAgency();

    IParentRef getParentRef();
}
