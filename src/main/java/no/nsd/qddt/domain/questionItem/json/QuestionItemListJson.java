package no.nsd.qddt.domain.questionItem.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.questionItem.QuestionItem;
import no.nsd.qddt.domain.responsedomain.json.ResponseDomainJsonView;
import no.nsd.qddt.domain.user.UserJson;
import org.hibernate.annotations.Type;

import javax.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Stig Norland
 */
public class QuestionItemListJson {

    @Type(type = "pg-uuid")
    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

    @Embedded
    private Version version;

    private String question;

    private String intent;

    private ResponseDomainJsonView responseDomain;


    public QuestionItemListJson() {
    }

    public QuestionItemListJson(QuestionItem entity) {
        if (entity == null) return;
        id = entity.getId();
        name = entity.getName();
        agency = new AgencyJsonView(entity.getAgency());
        version = entity.getVersion();
        modified = entity.getModified();
        modifiedBy = new UserJson(entity.getModifiedBy());
        question = entity.getQuestion();
        intent = entity.getIntent();
        responseDomain = new ResponseDomainJsonView(entity.getResponseDomain());
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


    public LocalDateTime getModified() {
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


    public ResponseDomainJsonView getResponseDomain() {
        return responseDomain;
    }

}