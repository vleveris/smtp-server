package smtp.server.admin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Mail;

import java.util.Set;

@Repository
public interface MailRepository extends CrudRepository<Mail, Long> {
    Mail findMailByAccountsInAndId(Set<Account> accounts, Long id);
}
