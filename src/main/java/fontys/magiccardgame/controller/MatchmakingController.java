package fontys.magiccardgame.controller;

import fontys.magiccardgame.business.dto.GameStartMessage;
import fontys.magiccardgame.domain.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import fontys.magiccardgame.service.GameSessionService;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MatchmakingController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameSessionService gameSessionService;
    private final ConcurrentHashMap<Long, GameSession> activeGames = new ConcurrentHashMap<>();
    private Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
    private static final String TOPIC_GAME = "/topic/game/";

    public MatchmakingController(SimpMessagingTemplate messagingTemplate, GameSessionService gameSessionService) {
        this.messagingTemplate = messagingTemplate;
        this.gameSessionService = gameSessionService;
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
        if (message.getStatus().equals("Accepted")) {
            GameSession gameSession = gameSessionService.createGameSession(message);
            message.setGameId(gameSession.getId());
            messagingTemplate.convertAndSend("/topic/invitations/" + message.getSender(), message);
            activeGames.put(gameSession.getId(), gameSession);

        }
        messagingTemplate.convertAndSend("/topic/invitations/" + message.getReceiver(), message);
    }

    @MessageMapping("/game/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public GameSession receiveSession(GameStartMessage message) {
        messagingTemplate.convertAndSend(TOPIC_GAME + message.getGameId(), activeGames.get(message.getGameId()));
        return activeGames.get(message.getGameId());
    }

    @MessageMapping("/play-card")
    public void playCard(PlayCardRequest request) {
        GameSession gameSession = activeGames.get(request.getGameId());
        if (gameSession != null) {
            try {
                gameSession.addPlayCardRequest(request);
            } catch (IllegalStateException e) {
                // Handle case where player tries to play more than one card
                messagingTemplate.convertAndSend(TOPIC_GAME + gameSession.getId(), e.getMessage());
            }

            if (gameSession.isGameOver()) {
                messagingTemplate.convertAndSend(TOPIC_GAME + gameSession.getId(), gameSession);
                activeGames.remove(gameSession.getId());
            } else {
                if (gameSession.getPlayersWhoPlayed().size()<2){
                    invitePlayersToPlay(gameSession);
                }

            }
            if (gameSession.isTurnFinished()) {
                messagingTemplate.convertAndSend(TOPIC_GAME + gameSession.getId(), gameSession);
                gameSession.getPendingRequests().clear();
                gameSession.getPlayersWhoPlayed().clear();
            }

        }

    }

    private void invitePlayersToPlay(GameSession gameSession) {
        String message = "Opponent has played a card.";
        if (gameSession.getPlayersWhoPlayed().contains(gameSession.getPlayer1().getUserId())) {
            messagingTemplate.convertAndSend(TOPIC_GAME + gameSession.getId() + "/player/" + gameSession.getPlayer2().getUserId(), message);
        } else {
            messagingTemplate.convertAndSend(TOPIC_GAME + gameSession.getId() + "/player/" + gameSession.getPlayer1().getUserId(), message);
        }
    }
}