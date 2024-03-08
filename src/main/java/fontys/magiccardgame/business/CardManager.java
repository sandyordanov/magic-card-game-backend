package fontys.magiccardgame.business;

import fontys.magiccardgame.models.Card;

import java.util.List;

public interface CardManager {
    List<Card> getAllCards();
    Card getById(long id);
    void save (Card card);
    void deleteById(long cardId);
    boolean updateCard(Card card);
}
