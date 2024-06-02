package fontys.magiccardgame.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameInvitation {
    private String sender;
    private String receiver;
    private String status;
    private String senderName;


}