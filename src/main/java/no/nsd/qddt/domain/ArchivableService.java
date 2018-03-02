package no.nsd.qddt.domain;

import java.util.UUID;

/**
 * @author Stig Norland
 */
public interface ArchivableService<T extends AbstractEntityAudit> extends BaseService<T,UUID>{

    default T doArchive(T instance) {
        try {
            if (instance.getChangeKind() == AbstractEntityAudit.ChangeKind.ARCHIVED) {
                String changecomment =  instance.getChangeComment();
                instance = findOne(  instance.getId() );
                ((Archivable)instance).setArchived(true);
                instance.setChangeComment(changecomment);
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return instance;
    }

}