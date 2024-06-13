package fontys.magiccardgame.business.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(){
        super( "Username already exists");
    }
}
