package smtp.server.admin.services;

import smtp.server.admin.models.Account;
import smtp.server.admin.models.Mail;

import java.util.Set;

public interface IMailService {
    Mail findMailByAccountsInAndId(Set<Account> accounts, Long id);
}
