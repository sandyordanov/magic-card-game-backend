package fontys.magiccardgame.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credentials did not match");
    }
}
