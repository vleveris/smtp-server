package smtp.server.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Mail;
import smtp.server.admin.repositories.MailRepository;

import java.util.Set;

@Service

public class MailService implements IMailService {
    @Autowired
    private MailRepository repository;

    @Override
    public Mail findMailByAccountsInAndId(Set<Account> accounts, Long id) {
        return repository.findMailByAccountsInAndId(accounts, id);
    }
}
