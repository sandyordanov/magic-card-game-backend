package fontys.magiccardgame.ControllerTests;

import fontys.magiccardgame.business.CardService;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.AccessTokenDecoder;
import fontys.magiccardgame.configuration.security.token.impl.AccessTokenImpl;
import fontys.magiccardgame.controller.CardController;
import fontys.magiccardgame.domain.Card;
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

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CardController.class)
class CardControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CardService cardService;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @Nested
    class WithAdminAccess {
        @BeforeEach
        void setUp() throws Exception {
            AccessToken accessToken1 = new AccessTokenImpl("jeff", 1L, RoleEnum.ADMIN);
            when(accessTokenDecoder.decode(anyString())).thenReturn(accessToken1);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void getCards_shouldReturn200ResponseWithCardsArray() throws Exception {
            Card card = new Card(1L, "card", 10, 10);
            GetAllCardsResponse response = GetAllCardsResponse.builder().cards(List.of(card)).build();

            when(cardService.getAllCards()).thenReturn(response);
            mockMvc.perform(get("/cards"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"cards\":[{\"id\":1,\"name\":\"card\",\"healthPoints\":10,\"attackPoints\":10}]}"));

            verify(cardService).getAllCards();
        }


        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void createCard_shouldReturn200WhenSuccessfullyCreatingACard() throws Exception {
            Card card = new Card(1L, "card", 10, 10);

            when(cardService.save(any(Card.class))).thenReturn(card);
            mockMvc.perform(post("/cards")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"name\":\"card\",\"healthPoints\":10,\"attackPoints\":10}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"id\":1,\"name\":\"card\",\"healthPoints\":10,\"attackPoints\":10}"));

            verify(cardService).save(any(Card.class));
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void createCard_shouldNotCreateAndReturn400_WhenParametersAreInvalid() throws Exception {

            mockMvc.perform(post("/cards")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"name\":\"\",\"healthPoints\":100,\"attackPoints\":100}"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"name\":\"Name cannot be blank\",\"healthPoints\":\"Health points must be less than or equal to 20\",\"attackPoints\":\"Attack points must be less than or equal to 20\"}"));

            verifyNoInteractions(cardService);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void updateCard_shouldReturn200WhenSuccessfullyUpdatingACard() throws Exception {
            Card card = new Card(1L, "updatedCard", 15, 15);

            when(cardService.updateCard(any(Card.class))).thenReturn(card);
            mockMvc.perform(put("/cards/{id}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"name\":\"updatedCard\",\"healthPoints\":15,\"attackPoints\":15}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"id\":1,\"name\":\"updatedCard\",\"healthPoints\":15,\"attackPoints\":15}"));

            verify(cardService).updateCard(any(Card.class));
        }
        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void updateCard_shouldNotCreateAndReturn400_WhenParametersAreInvalid() throws Exception {
            Card card = new Card(1L, "updatedCard", 15, 15);

            when(cardService.updateCard(any(Card.class))).thenReturn(card);
            mockMvc.perform(put("/cards/{id}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"name\":\"\",\"healthPoints\":100,\"attackPoints\":100}"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                    .andExpect(content().json("{\"name\":\"Name cannot be blank\",\"healthPoints\":\"Health points must be less than or equal to 20\",\"attackPoints\":\"Attack points must be less than or equal to 20\"}"));

            verifyNoInteractions(cardService);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void deleteCard_shouldReturn204WhenSuccessfullyDeletingACard() throws Exception {
            doNothing().when(cardService).deleteById(1L);

            mockMvc.perform(delete("/cards/{cardId}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(cardService).deleteById(1L);
        }

        @Test
        @WithMockUser(username = "jeff", roles = {"ADMIN"})
        void deleteCard_shouldReturn404WhenCardNotFound() throws Exception {
            doThrow(new RuntimeException("Card not found")).when(cardService).deleteById(1L);

            mockMvc.perform(delete("/cards/{cardId}", 1L)
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(cardService).deleteById(1L);
        }
    }

    @Nested
    class WithoutAdminAccess {
        @BeforeEach
        void setUp() throws Exception {
            AccessToken accessToken1 = new AccessTokenImpl("user", 2L, RoleEnum.PLAYER);
            when(accessTokenDecoder.decode(anyString())).thenReturn(accessToken1);
        }
        @Test
        @WithMockUser(username = "user", roles = {"PLAYER"})
        void createCard_shouldNotCreateAndReturn403_WhenUserIsPlayer() throws Exception {

            mockMvc.perform(post("/cards")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"name\":\"card\",\"healthPoints\":10,\"attackPoints\":10}"))
                    .andDo(print())
                    .andExpect(status().isForbidden());
            verifyNoInteractions(cardService);
        }
        @Test
        @WithMockUser(username = "user", roles = {"PLAYER"})
        void updateCard_shouldReturn403_WhenUserIsPlayer() throws Exception {
            Card card = new Card(1L, "updatedCard", 15, 15);

            when(cardService.updateCard(any(Card.class))).thenReturn(card);
            mockMvc.perform(put("/cards/{id}", 1L)
                            .contentType(APPLICATION_JSON_VALUE)
                            .content("{\"name\":\"updatedCard\",\"healthPoints\":15,\"attackPoints\":15}"))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            verifyNoInteractions(cardService);
        }
        @Test
        @WithMockUser(username = "jeff", roles = {"PLAYER"})
        void deleteCard_shouldReturn403_WhenUserIsPlayer() throws Exception {

            mockMvc.perform(delete("/cards/{cardId}", 1L))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            verifyNoInteractions(cardService);
        }
    }
}

