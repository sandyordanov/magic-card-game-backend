package fontys.magiccardgame.business.exception;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(Long cardID) {
        super("Card with id " + cardID + "not found.");
    }
}
