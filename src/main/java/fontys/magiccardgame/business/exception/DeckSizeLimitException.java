package fontys.magiccardgame.business.exception;

public class DeckSizeLimitException extends RuntimeException {

    public DeckSizeLimitException(int deckSize) {
        super("Deck size limit exceeded. Max deck size is " + deckSize + ".");
    }
}
