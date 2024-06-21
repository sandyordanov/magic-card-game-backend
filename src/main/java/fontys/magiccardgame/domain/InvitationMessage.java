package fontys.magiccardgame.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class InvitationMessage {
    private String sender;
    private String receiver;
    private String status;
    private long gameId;
}