package fontys.magiccardgame;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.dto.GetDeckResponse;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.business.exception.DeckSizeLimitException;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.configuration.security.token.exception.UnauthorizedDataAccessException;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.DeckRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import fontys.magiccardgame.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class DeckServiceTests {
    @Mock
    private CardRepository cardRepo;
    @Mock
    private DeckRepository deckRepo;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private DeckService deckService;

    @Test
     void addCard_ShouldAddCardToThePlayersDeck() {
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(new ArrayList<>())
                .build();
        CardEntity card = CardEntity.builder()
                .id(1L)
                .name("Updated Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        PlayerEntity player = PlayerEntity.builder()
                .id(1L)
                .deck(deck)
                .ownedCards(List.of(card))
                .user(UserEntity.builder().id(1L).build())
                .build();
        deck.setPlayer(player);
        when(accessToken.getUserId()).thenReturn(1L);
        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));

        deckService.addCard(1L, 1L);

        verify(deckRepo).findById(1L);
        verify(cardRepo).findById(1L);
        verify(deckRepo).save(deck);
        assertTrue(player.getDeck().getCards().contains(card));
    }

    @Test
     void addCard_ShouldThrowAnException_WhenCardIdIsNotValid() {
        when(cardRepo.findById(1L)).thenThrow(CardNotFoundException.class);

        assertThrows(CardNotFoundException.class, () -> {
            deckService.addCard(1L, 1L);
        });
    }

    @Test
     void addCard_ShouldThrowAnException_WhenDeckIdIsNotValid() {
        CardEntity card = CardEntity.builder()
                .id(1L)
                .name("Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        when(deckRepo.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            deckService.addCard(1L, 1L);
        });
    }

    @Test
     void addCard_ShouldThrowAnException_WhenDeckSizeIsExceeded() {
        CardEntity card = CardEntity.builder()
                .id(1L)
                .name("Updated Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        List<CardEntity> cards = new ArrayList<>();
        while (cards.size() <= deckService.getDeckSize()) {
            cards.add(card);
        }
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(cards)
                .player(PlayerEntity.builder().user(UserEntity.builder().id(1L).build()).build())
                .build();
        when(accessToken.getUserId()).thenReturn(1L);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));

        assertThrows(DeckSizeLimitException.class, () -> {
            deckService.addCard(1L, 1L);
        });
    }

    @Test
     void addCard_ShouldThrowAnException_WhenPlayerDoesNotPossessTheCard() {
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(new ArrayList<>())
                .build();
        CardEntity card = CardEntity.builder()
                .id(1L)
                .name("Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        PlayerEntity player = PlayerEntity.builder()
                .id(1L)
                .deck(deck)
                .ownedCards(new ArrayList<>())
                .user(UserEntity.builder().id(1L).build())
                .build();
        deck.setPlayer(player);
        when(accessToken.getUserId()).thenReturn(1L);
        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(IllegalArgumentException.class, () -> {
            deckService.addCard(1L, 1L);
        });
    }

    @Test
     void addCard_ShouldThrowAnException_WhenNotAuthorised() {
        CardEntity card = CardEntity.builder()
                .id(1L)
                .name("Updated Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        List<CardEntity> cards = new ArrayList<>();
        while (cards.size() <= deckService.getDeckSize()) {
            cards.add(card);
        }
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(cards)
                .player(PlayerEntity.builder().user(UserEntity.builder().id(1L).build()).build())
                .build();
        when(accessToken.getUserId()).thenReturn(2L);
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));

        assertThrows(UnauthorizedDataAccessException.class, () -> {
            deckService.addCard(1L, 1L);
        });
    }

    @Test
     void getDeck_ShouldReturnDeck_WhenDeckExistsAndUserIsAuthorized() {
        // Arrange
        CardEntity card1 = CardEntity.builder().id(1L).name("Card 1").attackPoints(10).healthPoints(20).build();
        CardEntity card2 = CardEntity.builder().id(2L).name("Card 2").attackPoints(15).healthPoints(25).build();
        List<CardEntity> cards = Arrays.asList(card1, card2);

        UserEntity user = UserEntity.builder().id(1L).build();
        PlayerEntity player = PlayerEntity.builder().user(user).build();
        DeckEntity deck = DeckEntity.builder().id(1L).cards(cards).player(player).build();

        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(accessToken.getUserId()).thenReturn(1L);

        // Act
        GetDeckResponse response = deckService.getDeck(1L);

        // Assert
        verify(deckRepo).findById(1L);
        verify(accessToken).getUserId();
        assertEquals(2, response.getDeck().size());
        assertEquals("Card 1", response.getDeck().get(0).getName());
        assertEquals("Card 2", response.getDeck().get(1).getName());
    }

    @Test
     void getDeck_ShouldThrowException_WhenDeckDoesNotExist() {
        // Arrange
        when(deckRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deckService.getDeck(1L);
        });
        verify(deckRepo).findById(1L);
    }

    @Test
     void getDeck_ShouldThrowException_WhenUserIsNotAuthorized() {
        // Arrange
        CardEntity card1 = CardEntity.builder().id(1L).name("Card 1").attackPoints(10).healthPoints(20).build();
        CardEntity card2 = CardEntity.builder().id(2L).name("Card 2").attackPoints(15).healthPoints(25).build();
        List<CardEntity> cards = Arrays.asList(card1, card2);

        UserEntity user = UserEntity.builder().id(2L).build();
        PlayerEntity player = PlayerEntity.builder().user(user).build();
        DeckEntity deck = DeckEntity.builder().id(1L).cards(cards).player(player).build();

        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(accessToken.getUserId()).thenReturn(1L);

        // Act & Assert
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            deckService.getDeck(1L);
        });
        verify(deckRepo).findById(1L);
        verify(accessToken).getUserId();
    }

    @Test
     void removeCardFromDeck_ShouldReturnTrue_WhenCardIsRemoved() {
        // Arrange
        CardEntity card1 = CardEntity.builder().id(1L).name("Card 1").attackPoints(10).healthPoints(20).build();
        CardEntity card2 = CardEntity.builder().id(2L).name("Card 2").attackPoints(15).healthPoints(25).build();
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(new ArrayList<>(Arrays.asList(card1, card2)))
                .player(PlayerEntity.builder().user(UserEntity.builder().id(1L).build()).build())
                .build();

        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(accessToken.getUserId()).thenReturn(1L);

        // Act
        Boolean result = deckService.removeCardFromDeck(1L, 1L);

        // Assert
        verify(deckRepo).findById(1L);
        verify(accessToken).getUserId();
        verify(deckRepo).save(deck);
        assertTrue(result);
        assertFalse(deck.getCards().contains(card1));
    }

    @Test
     void removeCardFromDeck_ShouldReturnFalse_WhenCardIsNotInDeck() {
        // Arrange
        CardEntity card1 = CardEntity.builder().id(1L).name("Card 1").attackPoints(10).healthPoints(20).build();
        CardEntity card2 = CardEntity.builder().id(2L).name("Card 2").attackPoints(15).healthPoints(25).build();
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(Arrays.asList(card2))
                .player(PlayerEntity.builder().user(UserEntity.builder().id(1L).build()).build())
                .build();

        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(accessToken.getUserId()).thenReturn(1L);

        // Act
        Boolean result = deckService.removeCardFromDeck(1L, 1L);

        // Assert
        verify(deckRepo).findById(1L);
        verify(accessToken).getUserId();
        verify(deckRepo).save(deck);
        assertFalse(result);
        assertTrue(deck.getCards().contains(card2));
    }

    @Test
     void removeCardFromDeck_ShouldThrowException_WhenDeckDoesNotExist() {
        // Arrange
        when(deckRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            deckService.removeCardFromDeck(1L, 1L);
        });
        verify(deckRepo).findById(1L);
    }

    @Test
     void removeCardFromDeck_ShouldThrowException_WhenUserIsNotAuthorized() {
        // Arrange
        CardEntity card1 = CardEntity.builder().id(1L).name("Card 1").attackPoints(10).healthPoints(20).build();
        CardEntity card2 = CardEntity.builder().id(2L).name("Card 2").attackPoints(15).healthPoints(25).build();
        DeckEntity deck = DeckEntity.builder()
                .id(1L)
                .cards(Arrays.asList(card1, card2))
                .player(PlayerEntity.builder().user(UserEntity.builder().id(2L).build()).build())
                .build();

        when(deckRepo.findById(1L)).thenReturn(Optional.of(deck));
        when(accessToken.getUserId()).thenReturn(1L);

        // Act & Assert
        assertThrows(UnauthorizedDataAccessException.class, () -> {
            deckService.removeCardFromDeck(1L, 1L);
        });
        verify(deckRepo).findById(1L);
        verify(accessToken).getUserId();
    }

    @Test
     void getAverageHealthAndAttackPointsByDeck_ShouldReturnAverages_WhenDataExists() {
        // Arrange
        List<Object[]> mockResult = List.of(new Object[][]{new Object[]{10.0, 20.0}});
        when(deckRepo.findAverageHealthAndAttackPointsByDeckId(1L)).thenReturn(mockResult);

        // Act
        Map<String, Double> averages = deckService.getAverageHealthAndAttackPointsByDeck(1L);

        // Assert
        assertEquals(3, averages.size());
        assertEquals(10.0, averages.get("averageHealthPoints"));
        assertEquals(20.0, averages.get("averageAttackPoints"));
    }

    @Test
     void getAverageHealthAndAttackPointsByDeck_ShouldReturnEmptyMap_WhenNoDataExists() {
        // Arrange
        when(deckRepo.findAverageHealthAndAttackPointsByDeckId(1L)).thenReturn(List.of());

        // Act
        Map<String, Double> averages = deckService.getAverageHealthAndAttackPointsByDeck(1L);

        // Assert
        assertTrue(averages.isEmpty());
    }

    @Test
     void getAverageHealthAndAttackPointsByDeck_ShouldHandleNullValues() {
        // Arrange
        List<Object[]> mockResult = List.of(new Object[][]{new Object[]{null, null}});
        when(deckRepo.findAverageHealthAndAttackPointsByDeckId(1L)).thenReturn(mockResult);

        // Act
        Map<String, Double> averages = deckService.getAverageHealthAndAttackPointsByDeck(1L);

        // Assert
        assertEquals(3, averages.size());
        assertEquals(null, averages.get("averageHealthPoints"));
        assertEquals(null, averages.get("averageAttackPoints"));
    }
}