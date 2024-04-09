package net.expenses.recorder.auth;

import net.expenses.recorder.dao.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * @author Kazi Tanvir Azad
 */
public class JWTAuthenticationToken extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 6336155097845178734L;
    private User user;

    public JWTAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        if (this.user != null) {
            setAuthenticated(true);
        }
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user != null ? user : null;
    }
}
