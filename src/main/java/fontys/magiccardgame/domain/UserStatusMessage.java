package fontys.magiccardgame.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusMessage {
    private String username;
    private boolean online;
}