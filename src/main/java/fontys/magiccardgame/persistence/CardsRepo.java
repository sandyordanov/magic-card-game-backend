package fontys.magiccardgame.persistence;

import fontys.magiccardgame.models.Card;

import java.util.List;

public interface CardsRepo {

    List<Card> getAllCards();
    Card getById(long id);
    Card save (Card card);
    void deleteById(long cardId);
    boolean exists(long cardId);

}
