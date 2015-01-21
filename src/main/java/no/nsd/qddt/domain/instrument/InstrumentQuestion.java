package no.nsd.qddt.domain.instrument;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.ChangeReason;
import no.nsd.qddt.domain.Question;
import no.nsd.qddt.domain.User;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Stig Norland
 */

@Audited
@Entity
@Table(name = "instrument_question")
public class InstrumentQuestion {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "created")
    private Instrument instrument;



    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "change_id")
    private ChangeReason changeReason;

    private String changeComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChangeReason getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(ChangeReason changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeComment() {
        return changeComment;
    }

    public void setChangeComment(String changeComment) {
        this.changeComment = changeComment;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentQuestion that = (InstrumentQuestion) o;

        if (changeComment != null ? !changeComment.equals(that.changeComment) : that.changeComment != null)
            return false;
        if (changeReason != null ? !changeReason.equals(that.changeReason) : that.changeReason != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (createdBy != null ? !createdBy.equals(that.createdBy) : that.createdBy != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (instrument != null ? !instrument.equals(that.instrument) : that.instrument != null) return false;
        if (instrument != null ? !instrument.equals(that.instrument) : that.instrument != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (question != null ? !question.equals(that.question) : that.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (instrument != null ? instrument.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (changeReason != null ? changeReason.hashCode() : 0);
        result = 31 * result + (changeComment != null ? changeComment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InstrumentQuestion{" +
                "id=" + id +
                ", instrument=" + instrument +
                ", question=" + question +
                ", instrument=" + instrument +
                ", created=" + created +
                ", createdBy=" + createdBy +
                ", name='" + name + '\'' +
                ", changeReason=" + changeReason +
                ", changeComment='" + changeComment + '\'' +
                '}';
    }
}
