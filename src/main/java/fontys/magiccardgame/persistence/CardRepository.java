package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity,Long>{

}
