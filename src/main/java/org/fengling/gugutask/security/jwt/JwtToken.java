package org.fengling.gugutask.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
public class JwtToken extends UsernamePasswordAuthenticationToken {

    private String token;

    public JwtToken(String token) {
        super(null, null);
        this.token = token;
    }

    public JwtToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token) {
        super(principal, credentials, authorities);
        this.token = token;
    }

}
