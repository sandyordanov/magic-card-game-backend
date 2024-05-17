package fontys.magiccardgame;

import fontys.magiccardgame.business.LoginUseCase;
import fontys.magiccardgame.business.exception.InvalidCredentialsException;
import fontys.magiccardgame.configuration.security.token.AccessTokenEncoder;
import fontys.magiccardgame.domain.LoginRequest;
import fontys.magiccardgame.domain.LoginResponse;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.UserEntity;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    LoginUseCase loginUseCase;

//    @Test
//    void testLoginSuccess() {
//        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
//        UserEntity userEntity = UserEntity.builder()
//                .username("testUser")
//                .password("testPassword")
//                .role(RoleEnum.PLAYER).build();
//
//        when(userRepository.findByUsername(anyString())).thenReturn(userEntity);
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//        when(accessTokenEncoder.encode(any())).thenReturn("testToken");
//
//        LoginResponse loginResponse = loginUseCase.login(loginRequest);
//
//        assertNotNull(loginResponse);
//        assertEquals("testToken", loginResponse.getAccessToken());
//    }

    @Test
    void testLoginFailUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");

        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(loginRequest));
    }

    @Test
    void testLoginFailInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("wrongPassword");
        userEntity.setRole(RoleEnum.PLAYER);


        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.login(loginRequest));
    }
}
