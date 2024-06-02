package fontys.magiccardgame;

import fontys.magiccardgame.business.LoginService;
import fontys.magiccardgame.business.exception.InvalidCredentialsException;
import fontys.magiccardgame.configuration.security.token.AccessTokenEncoder;
import fontys.magiccardgame.business.dto.LoginRequest;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.UserEntity;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    LoginService loginService;

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

        assertThrows(InvalidCredentialsException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void testLoginFailInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("wrongPassword");
        userEntity.setRole(RoleEnum.PLAYER);


        assertThrows(InvalidCredentialsException.class, () -> loginService.login(loginRequest));
    }
}
