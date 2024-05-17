package fontys.magiccardgame;

import fontys.magiccardgame.business.DefaultCardsService;
import fontys.magiccardgame.business.UserService;
import fontys.magiccardgame.business.dto.CreateUserRequest;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.business.dto.GetAllUsersResponse;
import fontys.magiccardgame.business.dto.UpdateUserRequest;
import fontys.magiccardgame.business.exception.UsernameAlreadyExistsException;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.exception.UnauthorizedDataAccessException;
import fontys.magiccardgame.domain.User;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import fontys.magiccardgame.persistence.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    PlayerRepository playerRepository;
    @Mock
    DefaultCardsService defaultCardsService;
    @Mock
    private AccessToken accessToken;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;


    @Test
    public void getUser_ShouldReturnUserWith_WhenIdIsValid() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(accessToken.getUserId()).thenReturn(1L);

        Optional<User> user = userService.getUser(1L);

        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getUser_ShouldThrowExceptionWhenUnauthorised() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            userService.getUser(1L);
        });
    }
    @Test
    public void getUser_ShouldThrowExceptionWhenUnauthenticated() {
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(accessToken.getUserId()).thenReturn(2L);
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            userService.getUser(1L);
        });
    }
    @Test
    public void getAllUsers_ShouldReturnListOfUsersWhenRoleIsAdmin() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);

        GetAllUsersResponse result = userService.getAllUsers();

        assertEquals(2, result.getUsers().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getAllUsers_ShouldThrowExceptionWhenUnauthorised() {
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            userService.getUser(1L);
        });
    }

    @Test
    public void createUser_ShouldCreateUserWhenDataIsValid() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("test")
                .password("password")
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(request.getUsername())
                .password("encodedPassword")
                .role(RoleEnum.PLAYER)
                .build();
        when(userRepository.findByUsername("test")).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User user = userService.createUser(request);

        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(RoleEnum.PLAYER, user.getRole());
    }

    @Test
    public void createUser_ShouldThrowAnExceptionWhenUsernameAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(existingUser);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(request));
    }

    @Test
    public void updateUser_ShouldUpdateUserWithValidData(){
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        request.setPassword("newPassword");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(accessToken.getUserId()).thenReturn(1L);

        userService.updateUser(request);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
    @Test
    public void updateUser_ShouldThrowExceptionWhenUnauthorised() {
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            userService.updateUser(new UpdateUserRequest());
        });
    }
    @Test
    public void updateUser_ShouldThrowExceptionWhenUnauthenticated() {
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(accessToken.getUserId()).thenReturn(2L);
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            userService.updateUser(new UpdateUserRequest());
        });
    }
    @Test
    public void updateUser_ShouldThrowException_WhenUserIdIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        when(userRepository.findById(1L)).thenThrow( new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(request);
        });
    }

    @Test
    public void testDeleteUser() {
        when(accessToken.getUserId()).thenReturn(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
