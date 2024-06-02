package fontys.magiccardgame.persistence;

import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeckRepository extends JpaRepository<DeckEntity,Long> {
    @Query("SELECT AVG(c.healthPoints), AVG(c.attackPoints) FROM DeckEntity d JOIN d.cards c WHERE d.id = :deckId")
    List<Object[]> findAverageHealthAndAttackPointsByDeckId(@Param("deckId") Long deckId);
}
