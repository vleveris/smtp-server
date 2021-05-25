package smtp.server.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Domain;
import smtp.server.admin.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Override
    public List<Account> findAll() {

        return (List<Account>) repository.findAll();
    }

    @Override
    public Account findAccountByUsernameAndDomain(String username, Domain domain) {
        Optional<Account> account = repository.findAccountByUsernameAndDomain(username, domain);
        return account.orElse(null);
    }

    public List<Account> findAccountsByDomainId(long id) {
        return repository.findAccountsByDomainId(id);
    }

    public Account findAccountById(Long id) {
        return repository.findAccountById(id);
    }

    public Account save(Account account) {
        return repository.save(account);
    }

    public void delete(Account account) {
        repository.delete(account);
    }

}