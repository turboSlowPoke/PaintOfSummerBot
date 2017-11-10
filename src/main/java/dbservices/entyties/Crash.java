package dbservices.entyties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "crashes")
public class Crash {
    @Id @Column(name = "id")
    private long id;
    @Column(name = "type")
    private String type;
    @Column(name = "startdate")
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime closeDate;
    private LocalDateTime updatedDate;
    private User creator;
    private User closer;
    private User redactor;
}
