package dbservices.entyties;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id @Column(name = "id")
    private long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "telegramusername")
    private String telegramUsername;
    @Column(name = "telegramchatid")
    private long telegramChatId;
    @Column(name = "startdate")
    private LocalDateTime startDate;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Address address;

    public User() {
    }


    public User(long telegramChatId, String firstName, String lastName, String telegramUsername) {
        this.telegramChatId = telegramChatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telegramUsername = telegramUsername;
        this.startDate=LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "UserId: "+id+", firstName: "+firstName+", lastName: "+lastName+", chatId: "+telegramChatId;
    }

    public Address getAddress() {
        if (this.address==null)
            this.address=new Address();
        return this.address;
    }
}
