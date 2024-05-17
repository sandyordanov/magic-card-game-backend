package fontys.magiccardgame.business.dto;

import fontys.magiccardgame.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class GetAllUsersResponse {
    private List<User> users;
}
