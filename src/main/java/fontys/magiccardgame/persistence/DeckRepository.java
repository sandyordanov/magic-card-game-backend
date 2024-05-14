package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<DeckEntity,Integer> {

}
