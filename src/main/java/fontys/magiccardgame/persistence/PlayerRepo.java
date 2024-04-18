package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepo extends JpaRepository<Player,Integer> {
}
