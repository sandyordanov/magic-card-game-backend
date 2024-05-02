package fontys.magiccardgame.business;

import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.business.exceptions.CardNotFoundException;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardService {

    private CardRepository cardsRepo;


    public GetAllCardsResponse getAllCards() {
        List<Card> cards = cardsRepo.findAll()
                .stream()
                .map(CardConverter::convert)
                .toList();

        return GetAllCardsResponse.builder().cards(cards).build();
    }


    public Card getById(int id) {
        return CardConverter.convert(cardsRepo.findById(id).orElse(null)) ;
    }


    public CardEntity save(CardEntity card) {
        return cardsRepo.save(card);
    }


    public void deleteById(int cardId) {
        cardsRepo.deleteById(cardId);
    }


    public boolean updateCard(CardEntity newCard) {

        CardEntity card = cardsRepo.findById(newCard.getId()).orElseThrow(() -> new IllegalArgumentException("Cannot find card with the specified id."));
        card.setName(newCard.getName());
        card.setAttackPoints(newCard.getAttackPoints());
        card.setHealthPoints(newCard.getHealthPoints());
        return true;
    }
}
