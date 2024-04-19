package fontys.magiccardgame.business.exceptions;

public class DeckSizeLimitException extends Exception {

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
