package fontys.magiccardgame.business;

import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.PlayerRepo;
import fontys.magiccardgame.persistence.entity.Card;
import fontys.magiccardgame.persistence.entity.Player;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {
    @Autowired
    private DeckRepository deckRepo;
    @Autowired
    private CardRepository cardRepo;
    @Autowired
    private PlayerRepo playerRepo;

    static final int DECK_SIZE = 3;

    public void addCard(int cardId, int playerId) {
        var player = playerRepo.getReferenceById(playerId);
        Card card = cardRepo.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        if (player.getOwnedCards().contains(card)) {
            player.getDeck().getCards().add(card);
            playerRepo.save(player);
        }
        throw new IllegalArgumentException("You do not have the given card in player's card collection");

    }

    private boolean CheckDeckSize(Player player) {
        int cardsInDeck = deckRepo.getCountById(player.getDeck().getId());
        return cardsInDeck <= DECK_SIZE;
    }

}

