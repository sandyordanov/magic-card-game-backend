package fontys.magiccardgame.ControllerTests;

import fontys.magiccardgame.business.UserService;
import fontys.magiccardgame.business.dto.CreateUserRequest;
import fontys.magiccardgame.business.dto.GetAllUsersResponse;
import fontys.magiccardgame.business.dto.UpdateUserRequest;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.AccessTokenDecoder;
import fontys.magiccardgame.configuration.security.token.impl.AccessTokenImpl;
import fontys.magiccardgame.controller.UserController;
import fontys.magiccardgame.domain.User;
import fontys.magiccardgame.persistence.entity.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Nested
    class WithAdminAccess {
        @BeforeEach
        void setUp() {
            AccessToken accessToken1 = new AccessTokenImpl("jeff", 1L, RoleEnum.ADMIN);
            when(accessTokenDecoder.decode(anyString())).thenReturn(accessToken1);
        }
        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void getUserById_shouldReturn200ResponseWithUser() throws Exception {
            User user = new User(1L, "user1", "password", RoleEnum.PLAYER);

            when(userService.getUser(1L)).thenReturn(Optional.of(user));
            mockMvc.perform(get("/users/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"id\":1,\"username\":\"user1\",\"password\":\"password\",\"role\":\"PLAYER\"}"));

            verify(userService).getUser(1L);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void getUserById_shouldReturn404WhenUserNotFound() throws Exception {
            when(userService.getUser(1L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/users/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(userService).getUser(1L);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void getAllUsers_shouldReturn200ResponseWithUsersArray() throws Exception {
            User user = new User(1L, "user1", "password", RoleEnum.PLAYER);
            GetAllUsersResponse response = GetAllUsersResponse.builder().users(List.of(user)).build();

            when(userService.getAllUsers()).thenReturn(response);
            mockMvc.perform(get("/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"users\":[{\"id\":1,\"username\":\"user1\",\"password\":\"password\",\"role\":\"PLAYER\"}]}"));

            verify(userService).getAllUsers();
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void createUser_shouldReturn200WhenSuccessfullyCreatingUser() throws Exception {
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("user1")
                    .password("password")
                    .build();
            User user = new User(1L,"user","password",RoleEnum.PLAYER);

            when(userService.createUser(any(CreateUserRequest.class))).thenReturn(user);
            mockMvc.perform(post("/users")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"username\":\"user1\",\"password\":\"password\"}"))
                    .andDo(print())
                    .andExpect(status().isOk());


            verify(userService).createUser(any(CreateUserRequest.class));
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void createUser_shouldReturn400WhenRequestIsInvalid() throws Exception {
            mockMvc.perform(post("/users")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"username\":\"\",\"password\":\"\"}"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void createAdmin_shouldReturn204WhenSuccessfullyCreatingAdmin() throws Exception {
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("admin1")
                    .password("password")
                    .build();

            doNothing().when(userService).createAdmin(any(CreateUserRequest.class));
            mockMvc.perform(post("/users/admin")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"username\":\"admin1\",\"password\":\"password\"}"))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(userService).createAdmin(any(CreateUserRequest.class));
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN", "PLAYER"})
        void updateUser_shouldReturn204WhenSuccessfullyUpdatingUser() throws Exception {
            UpdateUserRequest request = new UpdateUserRequest();
            request.setId(1L);
            request.setPassword("newpassword");

            doNothing().when(userService).updateUser(any(UpdateUserRequest.class));
            mockMvc.perform(put("/users/{id}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"id\":1,\"password\":\"newpassword\"}"))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(userService).updateUser(any(UpdateUserRequest.class));
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN", "PLAYER"})
        void deleteUser_shouldReturn204WhenSuccessfullyDeletingUser() throws Exception {
            doNothing().when(userService).deleteUser(1L);

            mockMvc.perform(delete("/users/{id}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(userService).deleteUser(1L);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN", "PLAYER"})
        void deleteUser_shouldReturn404WhenUserNotFound() throws Exception {
            doThrow(new RuntimeException("User not found")).when(userService).deleteUser(1L);

            mockMvc.perform(delete("/users/{id}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(userService).deleteUser(1L);
        }
    }
    @Nested
    class WithoutAdminAccess {
        @BeforeEach
        void setUp() {
            AccessToken accessToken1 = new AccessTokenImpl("user", 2L, RoleEnum.PLAYER);
            when(accessTokenDecoder.decode(anyString())).thenReturn(accessToken1);
        }

        @Test
        @WithMockUser(username = "user", roles = {"PLAYER"})
        void createAdmin_shouldReturn403WhenUserIsPlayer() throws Exception {
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("admin1")
                    .password("password")
                    .build();

            doNothing().when(userService).createAdmin(any(CreateUserRequest.class));
            mockMvc.perform(post("/users/admin")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"username\":\"admin1\",\"password\":\"password\"}"))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            verifyNoInteractions(userService);
        }
    }
}
