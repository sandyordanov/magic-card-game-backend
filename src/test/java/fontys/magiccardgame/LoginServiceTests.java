package fontys.magiccardgame;

import fontys.magiccardgame.business.LoginService;
import fontys.magiccardgame.configuration.security.token.AccessTokenEncoder;
import fontys.magiccardgame.configuration.security.token.impl.AccessTokenImpl;
import fontys.magiccardgame.business.exception.InvalidCredentialsException;
import fontys.magiccardgame.business.dto.LoginRequest;
import fontys.magiccardgame.business.dto.LoginResponse;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import fontys.magiccardgame.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class LoginServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginService loginService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        // Initialize a mock user
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(RoleEnum.ADMIN);
    }

    @Test
     void login_ShouldReturnAccessToken_WhenCredentialsAreValid() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "rawPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("mockAccessToken");

        // Act
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Assert
        assertNotNull(loginResponse);
        assertEquals("mockAccessToken", loginResponse.getAccessToken());
    }

    @Test
     void login_ShouldThrowInvalidCredentialsException_WhenUsernameIsInvalid() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("invalidUser", "rawPassword");
        when(userRepository.findByUsername("invalidUser")).thenReturn(null);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(loginRequest);
        });
    }

    @Test
     void login_ShouldThrowInvalidCredentialsException_WhenPasswordIsInvalid() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> {
            loginService.login(loginRequest);
        });
    }

    @Test
     void matchesPassword_ShouldReturnTrue_WhenPasswordMatches() {
        // Arrange
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        boolean matches = loginService.matchesPassword(rawPassword, encodedPassword);

        // Assert
        assertTrue(matches);
    }

    @Test
     void matchesPassword_ShouldReturnFalse_WhenPasswordDoesNotMatch() {
        // Arrange
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        boolean matches = loginService.matchesPassword(rawPassword, encodedPassword);

        // Assert
        assertFalse(matches);
    }

    @Test
     void generateAccessToken_ShouldReturnEncodedToken() {
        // Arrange
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("mockAccessToken");

        // Act
        String accessToken = loginService.generateAccessToken(mockUser);

        // Assert
        assertEquals("mockAccessToken", accessToken);
    }
}

