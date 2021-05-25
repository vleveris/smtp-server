package smtp.server.admin.models;

import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(nullable = false)
    private int role;
    @ManyToOne(fetch = FetchType.EAGER)
    private Domain domain;
    @ColumnTransformer(read = "pgp_sym_decrypt(password, 'SecretKey')", write = "pgp_sym_encrypt(?, 'SecretKey')")
    @Column(nullable = false)
    private String password;
    @ManyToMany(mappedBy = "accounts")
    private Set<Mail> mails;

    public Account() {
    }

    public Account(String username, String password, int role, Domain domain) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.domain = domain;
        mails = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getRole() {
        if (role == 0) {
            return "ADMIN";
        }
        return "USER";
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Set<Mail> getMails() {
        return mails;
    }


}