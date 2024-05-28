package fontys.magiccardgame.business.exception;

import org.springframework.web.server.ResponseStatusException;

public class DeckSizeLimitException extends RuntimeException {

    public DeckSizeLimitException(int deckSize) {
        super("Deck size limit exceeded. Max deck size is " + deckSize + ".");
    }
}
