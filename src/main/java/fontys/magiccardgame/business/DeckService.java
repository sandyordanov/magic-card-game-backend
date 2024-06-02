package fontys.magiccardgame.business;

import fontys.magiccardgame.business.converters.CardConverter;
import fontys.magiccardgame.business.dto.GetDeckResponse;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.business.exception.DeckSizeLimitException;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeckService {
    private DeckRepository deckRepo;
    private CardRepository cardRepo;
    static final int DECK_SIZE = 6;
    public int getDeckSize() {
        return DECK_SIZE;
    }

    public GetDeckResponse getDeck(Long deckId) {
        //TODO: authorisation
        DeckEntity deck = deckRepo.findById(deckId).orElseThrow(IllegalArgumentException::new);

        return GetDeckResponse.builder()
                .deck(deck.getCards().stream().map(CardConverter::convert).collect(Collectors.toList())).build();
    }

    public void addCard(Long deckId, Long cardId) {
        //TODO: authorisation
        CardEntity card = cardRepo.findById(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        DeckEntity deck = deckRepo.findById(deckId).orElseThrow(IllegalArgumentException::new);

        if (!checkDeckSize(deck)) {
            throw new DeckSizeLimitException(DECK_SIZE);
        }
        if (deck.getPlayer().getOwnedCards().stream().noneMatch(ownedCard -> ownedCard.getId().equals(card.getId()))) {
            throw new IllegalArgumentException("You do not have the given card in player's card collection");
        }

        deck.getCards().add(card);
        deckRepo.save(deck);
    }

    public Boolean removeCardFromDeck(Long deckId, Long cardId) {
        //TODO: authorisation
        DeckEntity deck = deckRepo.findById(deckId).orElseThrow(IllegalArgumentException::new);

        Boolean result = deck.getCards().removeIf(cardEntity -> cardEntity.getId().equals(cardId));
        deckRepo.save(deck);
        return result;
    }

    public Map<String, Double> getAverageHealthAndAttackPointsByDeck(Long deckId) {
        List<Object[]> result = deckRepo.findAverageHealthAndAttackPointsByDeckId(deckId);
        Map<String, Double> averages = new HashMap<>();
        if (!result.isEmpty()) {
            Object[] averagesArray = result.get(0);
            averages.put("averageHealthPoints", (Double) averagesArray[0]);
            averages.put("averageAttackPoints", (Double) averagesArray[1]);
        }
        return averages;
    }

    //support methods
    private boolean checkDeckSize(DeckEntity deck) {
        int cardsInDeck = deck.getCards().size();
        return cardsInDeck < DECK_SIZE;
    }


}

