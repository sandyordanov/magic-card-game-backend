package fontys.magiccardgame.business.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credentials did not match");
    }
}
