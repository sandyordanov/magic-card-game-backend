package fontys.magiccardgame.business.impl;

import fontys.magiccardgame.business.CardServiceInterface;
import fontys.magiccardgame.models.Card;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CardService implements CardServiceInterface {
    private CardsRepo cardsRepo;


    @Override
    public List<Card> getAllCards() {
        return  cardsRepo.getAllCards();
    }

    @Override
    public Card getById(long id) {
        return cardsRepo.getById(id);
    }

    @Override
    public Card save(Card card) {
       return cardsRepo.save(card);
    }

    @Override
    public void deleteById(long cardId) {
        cardsRepo.deleteById(cardId);
    }

    @Override
    public boolean updateCard(Card card) {
        if(!cardsRepo.exists(card.getId())){
            return false;
        }
        Card toBeUpdated = cardsRepo.getById(card.getId());

        toBeUpdated.setName(card.getName());
        toBeUpdated.setAttackPoints(card.getAttackPoints());
        toBeUpdated.setHealthPoints(card.getHealthPoints());

        return true;
    }
}
