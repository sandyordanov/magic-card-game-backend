package fontys.magiccardgame.business;

import fontys.magiccardgame.business.exceptions.DeckSizeLimitException;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.PlayerRepo;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.Player;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {

    private DeckRepository deckRepo;

    private CardRepository cardRepo;

    private PlayerRepo playerRepo;

    static final int DECK_SIZE = 3;


    @SneakyThrows
    public void addCard(int cardId, int playerId) {
        Player player = playerRepo.findById(playerId).orElseThrow(() -> new IllegalArgumentException("Cannot find player with the specified id."));
        CardEntity card = cardRepo.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (!CheckDeckSize(player)) {
            throw new DeckSizeLimitException(DECK_SIZE);
        }

        if (player.getOwnedCards().contains(card)) {
            player.getDeck().getCards().add(card);
            playerRepo.save(player);
        }
        throw new IllegalArgumentException("You do not have the given card in player's card collection");

    }

    private boolean CheckDeckSize(Player player) {
        int cardsInDeck = player.getDeck().getCards().size();
        return cardsInDeck <= DECK_SIZE;
    }

}

