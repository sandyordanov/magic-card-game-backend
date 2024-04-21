package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Card,Integer> {

}
