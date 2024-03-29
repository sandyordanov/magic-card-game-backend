package fontys.magiccardgame.persistence.impl;

import fontys.magiccardgame.models.Card;
import fontys.magiccardgame.persistence.CardsRepo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CardsRepoFake implements CardsRepo {

    private final List<Card> cards;

    public CardsRepoFake() {
        cards = new ArrayList<>(Arrays.asList(
                new Card(1, "Human", 3, 4),
                new Card(2, "Metal", 1, 4),
                new Card(3, "Magic", 3, 1)
        ));
    }

    @Override
    public List<Card> getAllCards() {
        return Collections.unmodifiableList(cards);
    }

    @Override
    public Card getById(long id) {
        Optional<Card> optionalCard = this.cards
                .stream()
                .filter(card -> card.getId() == id)
                .findFirst();

        return optionalCard.orElse(null);
    }

    @Override
    public Card save(Card card) {
        card.setId(cards.size() + 1);
        cards.add(card);
        return card;
    }

    @Override
    public void deleteById(long cardId) {
        cards.removeIf(card -> card.getId() == (cardId));
    }

    @Override
    public boolean exists(long cardId) {
        for (Card c : cards) {
            if (c.getId() == cardId) {
                return true;
            }
        }
        return false;
    }

}
