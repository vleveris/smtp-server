package smtp.server.admin.services;

import smtp.server.admin.models.Account;
import smtp.server.admin.models.Domain;

import java.util.List;

public interface IAccountService {

    List<Account> findAll();

    List<Account> findAccountsByDomainId(long id);

    Account findAccountByUsernameAndDomain(String username, Domain domain);

    Account findAccountById(Long id);

    Account save(Account account);

    void delete(Account acount);

}