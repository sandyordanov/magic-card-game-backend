package fontys.magiccardgame.business;

import fontys.magiccardgame.models.Card;

import java.util.List;

public interface CardServiceInterface {
    List<Card> getAllCards();
    Card getById(long id);
    Card save (Card card);
    void deleteById(long cardId);
    boolean updateCard(Card card);
}
