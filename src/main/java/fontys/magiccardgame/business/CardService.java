package fontys.magiccardgame.business;

import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.Card;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardService {

    private CardRepository cardsRepo;


    public List<Card> getAllCards() {
        return cardsRepo.findAll();
    }


    public Optional<Card> getById(int id) {
        return cardsRepo.findById(id);
    }


    public Card save(Card card) {
        return cardsRepo.save(card);
    }


    public void deleteById(int cardId) {
        cardsRepo.deleteById(cardId);
    }


    public boolean updateCard(Card newCard) {

        Card card = cardsRepo.findById(newCard.getId()).orElseThrow(() -> new IllegalArgumentException("Cannot find card with the specified id."));
        card.setName(newCard.getName());
        card.setAttackPoints(newCard.getAttackPoints());
        card.setHealthPoints(newCard.getHealthPoints());
        return true;
    }
}
