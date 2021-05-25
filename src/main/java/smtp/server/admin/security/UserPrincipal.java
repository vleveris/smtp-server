package smtp.server.admin.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import smtp.server.admin.models.Account;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final Account account;

    public UserPrincipal(Account account) {
        this.account = account;
    }

    @Override
    public String getUsername() {
        return account.getUsername() + "@" + account.getDomain().getName();
    }

    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (account.getRole().equals("ADMIN"))
            return List.of(() -> "ROLE_ADMIN");
        else
            return List.of(() -> "ROLE_USER");

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}