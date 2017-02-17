package no.nsd.qddt.domain.question;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.comment.Comment;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class QuestionJson {

    @Type(type="pg-uuid")
    private UUID id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime modified;

    private Set<QuestionJson> children = new HashSet<>();

    private String questionIdxRationale;

    private String intent;

    private String question;

    public QuestionJson() {
    }

    public QuestionJson(Question question) {
        setModified(question.getModified());
        setId(question.getId());
        setChildren(question.getChildren().stream().map(F-> new QuestionJson(F)).collect(Collectors.toSet()));
        setQuestionIdxRationale(question.getQuestionIdxRationale());
        setIntent(question.getIntent());
        setQuestion(question.getQuestion());
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public Set<QuestionJson> getChildren() {
        return children;
    }

    public void setChildren(Set<QuestionJson> children) {
        this.children = children;
    }

    public String getQuestionIdxRationale() {
        return questionIdxRationale;
    }

    public void setQuestionIdxRationale(String questionIdxRationale) {
        this.questionIdxRationale = questionIdxRationale;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
