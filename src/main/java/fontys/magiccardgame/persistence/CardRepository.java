package fontys.magiccardgame.persistence;

import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CardRepository extends JpaRepository<CardEntity,Long>, JpaSpecificationExecutor<Card> {

}
