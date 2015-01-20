package no.nsd.qddt.domain.respons;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import no.nsd.qddt.domain.ChangeReason;
import no.nsd.qddt.domain.User;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Stig Norland
 */
@Audited
@Entity
@Table(name = "ResponsDomain")
public class ResponsDomain {

    private ResponsKind kind;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    private String name;

    @ManyToOne
    @JoinColumn(name = "change_id")
    private ChangeReason changeReason;

    private String changeComment;


    @ManyToOne
    @JoinColumn(name = "respons_kind_id")
    private ResponsKind responsKind;

/*    @ManyToMany(mappedBy="categoryCode", cascade = CascadeType.ALL)
    private Set<CategoryCode> categoryCode = new HashSet<>();
*/


}
