package fontys.magiccardgame.configuration.security.token.impl;

import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final Set<String> roles;

    public AccessTokenImpl(String subject, Long userId, RoleEnum role) {
        this.subject = subject;
        this.userId = userId;
        this.roles = Collections.singleton(role.toString());
    }

    @Override
    public boolean hasRole(String roleName) {
        return this.roles.contains(roleName);
    }
}
