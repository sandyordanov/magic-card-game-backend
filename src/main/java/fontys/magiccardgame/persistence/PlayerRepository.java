package fontys.magiccardgame.persistence;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity,Long> {
    PlayerEntity findByUser_Id(Long userId);
}
