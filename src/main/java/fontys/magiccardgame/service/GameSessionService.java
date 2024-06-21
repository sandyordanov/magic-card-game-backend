package fontys.magiccardgame.service;

import fontys.magiccardgame.business.converters.PlayerConverter;
import fontys.magiccardgame.business.GameSession;
import fontys.magiccardgame.domain.InvitationMessage;
import fontys.magiccardgame.domain.Player;
import fontys.magiccardgame.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameSessionService {
    private final PlayerRepository playerRepository;


    @Autowired
    public GameSessionService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public GameSession createGameSession(InvitationMessage invitation) {

        Player player1 = PlayerConverter.convert(playerRepository.findByName(invitation.getSender()));
        Player player2 = PlayerConverter.convert(playerRepository.findByName(invitation.getReceiver()));
        return new GameSession(player1, player2);
    }
}
