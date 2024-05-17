package fontys.magiccardgame.business.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String password;
}
