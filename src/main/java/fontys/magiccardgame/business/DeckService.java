package fontys.magiccardgame.business;

import fontys.magiccardgame.business.dto.AddCardToDeckRequest;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.business.exception.DeckSizeLimitException;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {

    private DeckRepository deckRepo;

    private CardRepository cardRepo;

    private PlayerRepository playerRepository;

    static final int DECK_SIZE = 3;

    public void addCard(AddCardToDeckRequest request) {
        PlayerEntity player = playerRepository.findById(request.getPlayerId()).orElseThrow(
                () -> new IllegalArgumentException("Cannot find player with the specified id."));
        CardEntity card = cardRepo.findById(request.getCardId()).orElseThrow(() -> new CardNotFoundException(request.getCardId()));

        if (!checkDeckSize(player)) {
            throw new DeckSizeLimitException(DECK_SIZE);
        }

        if (player.getOwnedCards().stream().noneMatch(ownedCard -> ownedCard.getId().equals(card.getId()))) {
            throw new IllegalArgumentException("You do not have the given card in player's card collection");
        }
        player.getDeck().getCards().add(card);
        playerRepository.save(player);
    }

    private boolean checkDeckSize(PlayerEntity player) {
        int cardsInDeck = player.getDeck().getCards().size();
        return cardsInDeck <= DECK_SIZE;
    }

    public int getDeckSize(){
        return DECK_SIZE;
    }
}

