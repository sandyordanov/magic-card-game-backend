package fontys.magiccardgame.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
