package fontys.magiccardgame.business.exception;

public class DeckSizeLimitException extends RuntimeException {

    private String message;

    public DeckSizeLimitException() {
        super("Deck size limit exceeded.");
    }

    public DeckSizeLimitException(String message) {
        super(message);
    }

    public DeckSizeLimitException(int deckSize) {
        super("Deck size limit exceeded. Max deck size is " + deckSize + ".");
    }
}
