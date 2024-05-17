package fontys.magiccardgame.business.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String username;
    private String password;
}
