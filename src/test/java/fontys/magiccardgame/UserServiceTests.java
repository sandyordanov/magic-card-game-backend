package fontys.magiccardgame;

import fontys.magiccardgame.business.DefaultCards;
import fontys.magiccardgame.business.PlayerService;
import fontys.magiccardgame.business.UserService;
import fontys.magiccardgame.business.dto.CreateUserRequest;
import fontys.magiccardgame.business.dto.GetAllUsersResponse;
import fontys.magiccardgame.business.dto.UpdateUserRequest;
import fontys.magiccardgame.business.exception.UsernameAlreadyExistsException;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.exception.UnauthorizedDataAccessException;
import fontys.magiccardgame.domain.User;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.UserRepository;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import fontys.magiccardgame.persistence.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.configuration.GlobalConfiguration.validate;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class UserServiceTests {

    @Mock
    UserRepository userRepository;
    @Mock
    private AccessToken accessToken;
    @Mock
    private DefaultCards defaultCards;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;


    @Test
     void getUser_ShouldReturnUserWith_WhenIdIsValid() {
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
     void getUser_ShouldReturnUserWith_WhenAskedByAdmin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);

        Optional<User> user = userService.getUser(1L);

        assertTrue(user.isPresent());
        assertEquals(1L, user.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
     void getUser_ShouldThrowExceptionWhenUnauthorised() {
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        assertThrows(UnauthorizedDataAccessException.class, () -> userService.getUser(1L));
    }
    @Test
     void getUser_ShouldThrowExceptionWhenUnauthenticated() {
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(accessToken.getUserId()).thenReturn(2L);
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            userService.getUser(1L);
        });
    }
    @Test
     void getAllUsers_ShouldReturnListOfUsersWhenRoleIsAdmin() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);

        GetAllUsersResponse result = userService.getAllUsers();

        assertEquals(2, result.getUsers().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
     void getAllUsers_ShouldThrowExceptionWhenUnauthorised() {
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        assertThrows(UnauthorizedDataAccessException.class, () -> userService.getAllUsers());
    }

    @Test
     void createUser_ShouldCreateUserWhenDataIsValid() {
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
     void createUser_ShouldThrowAnExceptionWhenUsernameAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(existingUser);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(request));
    }
    @Test
     void createAdmin_ShouldCreateAdmin_WhenDataIsValid() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("test")
                .password("password")
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(request.getUsername())
                .password("encodedPassword")
                .role(RoleEnum.ADMIN)
                .build();
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        when(userRepository.findByUsername("test")).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);


        userService.createAdmin(request);

        verify(userRepository,times(1)).save(any(UserEntity.class));
    }
    @Test
     void createAdmin_ShouldThrowAnExceptionWhenUsernameAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("test");
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        when(userRepository.findByUsername("test")).thenReturn(existingUser);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createAdmin(request));
    }
    @Test
     void createAdmin_ShouldThrowAnException_WhenNotAuthorised() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");

        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);

        assertThrows(UnauthorizedDataAccessException.class, () -> userService.createAdmin(request));
    }
    @Test
     void updateUser_ShouldUpdateUserWithValidData(){
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
     void updateUser_ShouldThrowExceptionWhenUnauthorised() {
       UpdateUserRequest request = new UpdateUserRequest();
       request.setId(1L);
       request.setPassword("newPassword");
        assertThrows(UnauthorizedDataAccessException.class, () -> userService.updateUser(request));
    }
    @Test
     void updateUser_ShouldThrowExceptionWhenUnauthenticated() {
       UpdateUserRequest request = new UpdateUserRequest();
       request.setId(1L);
       request.setPassword("newPassword");
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(accessToken.getUserId()).thenReturn(2L);
        assertThrows(UnauthorizedDataAccessException.class, () -> userService.updateUser(request));
    }
    @Test
     void updateUser_ShouldThrowException_WhenUserIdIsInvalid() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(1L);
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        when(userRepository.findById(1L)).thenThrow( new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(request));
    }

    @Test
     void testDeleteUser() {
        when(accessToken.getUserId()).thenReturn(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
    @Test
     void deleteUser_ShouldThrowException_WhenNotAuthorisedUser() {
        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> userService.deleteUser(1L));
    }
    @Test
     void deleteUser_ShouldDeleteUser_WhenUserIsAnAdmin() {
        when(accessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
