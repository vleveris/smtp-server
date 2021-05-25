package smtp.server.admin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Domain;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findAccountByUsernameAndDomain(String username, Domain domain);

    List<Account> findAccountsByDomainId(long id);

    Account findAccountById(Long id);

}