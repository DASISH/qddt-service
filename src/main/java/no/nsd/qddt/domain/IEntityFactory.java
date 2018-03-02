package no.nsd.qddt.domain;

import no.nsd.qddt.domain.AbstractEntityAudit.ChangeKind;


public interface IEntityFactory<T extends AbstractEntityAudit> {


    T create();

    T copyBody(T source, T dest);

    default T copy(T source,  Long revision) {
        if (source.isNewCopy())
            revision = null;

        return copyBody(source, 
            makeNewCopy(source, revision));
    }

    default T makeNewCopy(T source, Long revision)
    {
        T retval = create();
        if (revision != null) {
            retval.setBasedOnObject(source.getId());
            retval.setBasedOnRevision(revision);
            retval.setChangeKind( ChangeKind.BASED_ON );
            retval.setChangeComment("based on " + source.getName() );
        } else {
            retval.setChangeKind(ChangeKind.NEW_COPY);
            retval.setChangeComment("copy of " + source.getName() );
        }
        retval.getVersion().setVersionLabel("COPY OF [" + source.getName() + "]");
        if(source instanceof Archivable)
            ((Archivable)retval).setArchived(false);
        //retval.setId(UUID.randomUUID());
        return retval;
    }


}