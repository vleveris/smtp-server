package smtp.server.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Domain;
import smtp.server.admin.repositories.AccountRepository;
import smtp.server.admin.repositories.DomainRepository;

import java.util.Optional;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DomainRepository domainRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        String[] accountParams = email.split("@", 2);
        Domain domain = domainRepository.findDomainByName(accountParams[1]);
        if (domain == null) {
            throw new UsernameNotFoundException("Invalid domain name");
        }
        Optional<Account> account = accountRepository.findAccountByUsernameAndDomain(accountParams[0], domain);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("Invalid account name");
        }
        return new UserPrincipal(account.get());
    }
}