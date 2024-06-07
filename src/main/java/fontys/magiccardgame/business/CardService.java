package fontys.magiccardgame.business;

import fontys.magiccardgame.business.converters.CardConverter;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.domain.CardSpecification;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Card getById(Long id) {
        return CardConverter.convert(cardsRepo.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id)));
    }
    public GetAllCardsResponse searchCards(String name, Integer minHealthPoints, Integer maxHealthPoints, Integer minAttackPoints, Integer maxAttackPoints) {
        Specification<Card> spec = CardSpecification.getCardsByFilters(name, minHealthPoints, maxHealthPoints, minAttackPoints, maxAttackPoints);
        return GetAllCardsResponse.builder().cards(cardsRepo.findAll(spec)).build();
    }

    public Card save(Card card) {
        CardEntity entity = CardEntity.builder()
                .name(card.getName())
                .attackPoints(card.getAttackPoints())
                .healthPoints(card.getHealthPoints())
                .build();
        CardEntity savedCard = cardsRepo.save(entity);
        return CardConverter.convert(savedCard);
    }


    public void deleteById(Long cardId) {
        cardsRepo.deleteById(cardId);
    }


    public Card updateCard(Card updatedCard) {
        CardEntity card = cardsRepo.findById(updatedCard.getId()).orElseThrow(() -> new IllegalArgumentException("Cannot find card with the specified id."));
        card.setName(updatedCard.getName());
        card.setAttackPoints(updatedCard.getAttackPoints());
        card.setHealthPoints(updatedCard.getHealthPoints());
        cardsRepo.save(card);
        return CardConverter.convert(card);
    }
}
