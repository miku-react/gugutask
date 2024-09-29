package org.fengling.gugutask.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter

public class JwtToken extends UsernamePasswordAuthenticationToken {

    private String token;
    private Long userId;

    public JwtToken(String token) {
        super(null, null);
        this.token = token;
    }

    public JwtToken(Long userId, Collection<? extends GrantedAuthority> authorities, String token) {
        super(userId, null, authorities); // userId作为principal，credentials为null
        this.userId = userId;
        this.token = token;
    }


}
