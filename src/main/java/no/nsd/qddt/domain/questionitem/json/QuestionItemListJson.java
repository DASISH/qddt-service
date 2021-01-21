package no.nsd.qddt.domain.questionitem.json;

import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.classes.interfaces.Version;
import no.nsd.qddt.domain.questionitem.QuestionItem;
import no.nsd.qddt.domain.user.json.UserJson;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class QuestionItemListJson {

    @Type(type = "pg-uuid")
    private UUID id;

    private String name;

    private String classKind;
 
    private Timestamp modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    @Embedded
    private Version version;

    private String question;

    private String intent;

    private String responseDomainName;

//    private ResponseDomainJsonView responseDomain;


    public QuestionItemListJson() {
    }

    public QuestionItemListJson(QuestionItem entity) {
        if (entity == null) return;
        id = entity.getId();
        name = entity.getName();
        agency = new AgencyJsonView(entity.getAgency());
        version = entity.getVersion();
        modified = entity.getModified();
        modifiedBy = entity.getModifiedBy();
        question = entity.getQuestion();
        intent = entity.getIntent();
        responseDomainName =  (entity.getResponseDomainRef() != null) ? entity.getResponseDomainRef().getName() : "";
        classKind = "QUESTION_ITEM";
//        responseDomain = new ResponseDomainJsonView(entity.getResponseDomain());
//        responseDomain.getVersion().setRevision(entity.getResponseDomainRevision());
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getQuestion() {
        return question;
    }


    public String getIntent() {
        return intent;
    }

    public String getResponseDomainName() {
        return responseDomainName;
    }

    public Timestamp getModified() {
        return modified;
    }


    public UserJson getModifiedBy() {
        return modifiedBy;
    }


    public AgencyJsonView getAgency() {
        return agency;
    }


    public Version getVersion() {
        return version;
    }

    public String getClassKind() {
        return classKind;
    }

}
