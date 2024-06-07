package fontys.magiccardgame.controller;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MatchmakingController {
    private final SimpMessagingTemplate messagingTemplate;
    private Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public MatchmakingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/status")
    @SendTo("/topic/online")
    public Set<String> updateStatus(UserStatusMessage message) {
        if (message.isOnline()) {
            onlineUsers.add(message.getUsername());
        } else {
            onlineUsers.remove(message.getUsername());
        }
        messagingTemplate.convertAndSend("/topic/online", onlineUsers);
        return onlineUsers;
    }

    @MessageMapping("/invite/{username}")
    @SendTo("/topic/invitations/{username}")
    public void sendInvitation(InvitationMessage message) {
        messagingTemplate.convertAndSend("/topic/invitations/" + message.getReceiver(), message);
    }
}
@Getter
@Setter
class UserStatusMessage {
    private String username;
    private boolean online;
}

@Getter
@Setter
class InvitationMessage {
    private String sender;
    private String receiver;
    private String status;
}
