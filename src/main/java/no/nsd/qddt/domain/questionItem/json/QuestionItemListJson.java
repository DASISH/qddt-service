package no.nsd.qddt.domain.questionItem.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.agency.AgencyJsonView;
import no.nsd.qddt.domain.embedded.Version;
import no.nsd.qddt.domain.question.Question;
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

    @Type(type="pg-uuid")
    private UUID id;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime modified;

    private UserJson modifiedBy;

    private AgencyJsonView agency;

//    @Type(type="pg-uuid")
//    private UUID basedOnObject;
//
//    private  Integer basedOnRevision;

    @Embedded
    private Version version;

    private Question question;

    private ResponseDomainJsonView responseDomain;

    private Integer responseDomainRevision;


    public QuestionItemListJson() {
    }

    public QuestionItemListJson(QuestionItem entity) {
        if (entity == null) return;
        id = entity.getId();
        name = entity.getName();
        agency = new AgencyJsonView(entity.getAgency());
        version =entity.getVersion();
//        basedOnObject = entity.getBasedOnObject();
//        basedOnRevision = entity.getBasedOnRevision();
        modified =entity.getModified();
        modifiedBy =new UserJson(entity.getModifiedBy());
        question =entity.getQuestion();
        responseDomain = new ResponseDomainJsonView(entity.getResponseDomain());
        responseDomainRevision = entity.getResponseDomainRevision();
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
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


    public Question getQuestion() {
        return question;
    }


    public ResponseDomainJsonView getResponseDomain() {
        return responseDomain;
    }

    public void setResponseDomain(ResponseDomainJsonView responseDomain) {
        this.responseDomain = responseDomain;
    }

//    private class ResponseDomainListJson {
//
//        private Version version;
//
//        private UUID id;
//
//        private String name;
//
//        public ResponseDomainListJson() {
//        }
//
//        public ResponseDomainListJson(String name) {
//            this.name = name;
//        }
//
//        public ResponseDomainListJson(ResponseDomain responseDomain) {
//            if (responseDomain != null)
//                this.name = responseDomain.getName();
//                this.id = responseDomain.getId();
//                this.version = responseDomain.getVersion();
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public UUID getId() {
//            return id;
//        }
//
//        public Version getVersion() {
//            return version;
//        }
//    }
}
