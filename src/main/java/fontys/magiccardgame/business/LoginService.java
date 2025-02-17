package fontys.magiccardgame.business;

import fontys.magiccardgame.configuration.security.token.AccessTokenEncoder;
import fontys.magiccardgame.configuration.security.token.impl.AccessTokenImpl;
import fontys.magiccardgame.business.exception.InvalidCredentialsException;
import fontys.magiccardgame.business.dto.LoginRequest;
import fontys.magiccardgame.business.dto.LoginResponse;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import fontys.magiccardgame.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {

            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generateAccessToken(UserEntity user) {

        RoleEnum role = user.getRole();
        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getUsername(), user.getId(), role));
    }

}
