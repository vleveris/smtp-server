package smtp.server.admin.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "mail")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String reversePath;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(columnDefinition = "text")
    private String message;
    @ManyToMany
    @JoinTable(name = "account_mail", joinColumns = @JoinColumn(name = "mail_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> accounts;

    public Mail(String reversePath, Date date, String message, Set<Account> accounts) {
        this.reversePath = reversePath;
        this.date = date;
        this.message = message;
        this.accounts = accounts;
    }

    public Mail() {
    }

    public Long getId() {
        return id;
    }

    public String getReversePath() {
        return reversePath;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }
}