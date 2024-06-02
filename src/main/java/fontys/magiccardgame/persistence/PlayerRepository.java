package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity,Long> {
    PlayerEntity findByUserId(Long userId);
}
