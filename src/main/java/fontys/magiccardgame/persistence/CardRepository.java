package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Integer>{

}
