package no.nsd.qddt.domain;

import no.nsd.qddt.domain.AbstractEntityAudit.ChangeKind;

import java.util.Optional;

/**
 * @author Stig Norland
 */


public interface IEntityFactory<T extends AbstractEntityAudit> {


    T create();

    T copyBody(T source, T dest);

    default T copy(T source, Optional<Integer> revision) {
        if (source.isNewCopy())
            revision = null;

        return copyBody(source, 
            makeNewCopy(source, revision));
    }

    default T makeNewCopy(T source, Optional<Integer> revision)
    {
        T retval = create();
        if (revision.isPresent()) {
            retval.setBasedOnObject(source.getId());
            retval.setBasedOnRevision(revision.get());
            retval.setChangeKind( ChangeKind.BASED_ON );
            retval.setChangeComment("based on " + source.getName() );
        } else {
            retval.setChangeKind(ChangeKind.NEW_COPY);
            retval.setChangeComment("copy of " + source.getName() );
        }
        retval.getVersion().setVersionLabel("COPY OF [" + source.getName() + "]");
        if(source instanceof IArchived)
            ((IArchived)retval).setArchived(false);
        retval.setClassKind( source.getClassKind() );
        retval.setName( source.getName() );
//        retval.setId( UUID.randomUUID());
        return retval;
    }


}