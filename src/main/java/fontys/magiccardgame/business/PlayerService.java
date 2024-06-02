package fontys.magiccardgame.business;

import fontys.magiccardgame.business.converters.CardConverter;
import fontys.magiccardgame.business.converters.PlayerConverter;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.domain.Player;
import fontys.magiccardgame.persistence.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerService {
    private PlayerRepository playerRepository;

    public Player getPlayer(Long userId) {
        //TODO: check if player exists
        return PlayerConverter.convert(playerRepository.findByUserId(userId));
    }

    public List<Player> getAllPlayers() {
        //TODO: check if player exists
        return playerRepository.findAll().stream().map(PlayerConverter::convert).collect(Collectors.toList());
    }

}
