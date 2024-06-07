package fontys.magiccardgame;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.PlayerService;
import fontys.magiccardgame.business.converters.PlayerConverter;
import fontys.magiccardgame.domain.Player;
import fontys.magiccardgame.configuration.security.token.AccessToken;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import fontys.magiccardgame.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import fontys.magiccardgame.configuration.security.token.exception.UnauthorizedDataAccessException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class PlayerServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private PlayerService playerService;

    private PlayerEntity mockPlayerEntity;

    @BeforeEach
    void setUp() {
        // Create mock PlayerEntity
        mockPlayerEntity = new PlayerEntity();
        mockPlayerEntity.setId(1L);
        mockPlayerEntity.setName("Player Name");

        // Create mock UserEntity
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(1L);
        mockPlayerEntity.setUser(mockUserEntity);

        // Create mock DeckEntity
        DeckEntity mockDeckEntity = new DeckEntity();
        mockDeckEntity.setId(1L);
        mockDeckEntity.setPlayer(mockPlayerEntity);
        mockDeckEntity.setCards(Collections.emptyList());
        mockPlayerEntity.setDeck(mockDeckEntity);

        // Ensure PlayerEntity has required relationships
        mockPlayerEntity.setOwnedCards(Collections.emptyList());
    }

    @Test
     void getPlayer_ShouldReturnPlayer_WhenUserIdMatches() {
        // Arrange
        Long userId = 1L;
        when(accessToken.getUserId()).thenReturn(userId);
        when(playerRepository.findByUserId(userId)).thenReturn(mockPlayerEntity);

        // Act
        Player player = playerService.getPlayer(userId);

        // Assert
        assertNotNull(player);
        assertEquals(mockPlayerEntity.getId(), player.getId());
        assertEquals(mockPlayerEntity.getName(), player.getName());
    }

    @Test
     void getPlayer_ShouldThrowUnauthorizedDataAccessException_WhenUserIdDoesNotMatch() {
        // Arrange
        Long userId = 1L;
        when(accessToken.getUserId()).thenReturn(2L);

        // Act & Assert
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            playerService.getPlayer(userId);
        });

        assertEquals("403 FORBIDDEN \"USER_ID_NOT_FROM_LOGGED_IN_USER\"", exception.getMessage());
    }

    @Test
     void getAllPlayers_ShouldReturnAllPlayers() {
        // Arrange
        PlayerEntity mockPlayerEntity1 = mockPlayerEntity;
        PlayerEntity mockPlayerEntity2 = mockPlayerEntity;
        when(playerRepository.findAll()).thenReturn(List.of(mockPlayerEntity1, mockPlayerEntity2));
        Player mockPlayer1 = PlayerConverter.convert(mockPlayerEntity1);
        Player mockPlayer2 = PlayerConverter.convert(mockPlayerEntity2);

        // Act
        List<Player> players = playerService.getAllPlayers();

        // Assert
        assertEquals(2, players.size());
        assertTrue(players.contains(mockPlayer1));
        assertTrue(players.contains(mockPlayer2));
    }

    @Test
     void getAllPlayers_ShouldReturnEmptyList_WhenNoPlayersExist() {
        // Arrange
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Player> players = playerService.getAllPlayers();

        // Assert
        assertTrue(players.isEmpty());
    }
}
