package fontys.magiccardgame.domain;

import fontys.magiccardgame.persistence.entity.RoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private RoleEnum role;
}
