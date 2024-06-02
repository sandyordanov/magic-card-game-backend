package fontys.magiccardgame.controller;

import fontys.magiccardgame.domain.GameInvitation;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class InvitationController {
    @MessageMapping("/invite/{userId}")
    @SendTo("/topic/invitations/{userId}")
    public GameInvitation sendInvitation(@DestinationVariable String userId, GameInvitation invitation) {
        return invitation;
    }
}