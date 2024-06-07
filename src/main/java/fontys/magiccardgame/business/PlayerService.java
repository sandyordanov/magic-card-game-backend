package fontys.magiccardgame.business;

import fontys.magiccardgame.business.converters.PlayerConverter;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.exception.UnauthorizedDataAccessException;
import fontys.magiccardgame.domain.Player;
import fontys.magiccardgame.persistence.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerService {
    private PlayerRepository playerRepository;
    private AccessToken requestAccessToken;
    public Player getPlayer(Long userId) {
        if (!requestAccessToken.getUserId().equals(userId)) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }
        return PlayerConverter.convert(playerRepository.findByUserId(userId));
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll().stream().map(PlayerConverter::convert).toList();
    }

}
