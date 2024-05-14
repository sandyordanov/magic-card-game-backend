package fontys.magiccardgame;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeckServiceTests {

    @Autowired
    private DeckService deckService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private CardRepository cardRepo;
    @Test
    public void testAddCard_CardNotOwnedByPlayer() {
        // Arrange
        PlayerEntity player = new PlayerEntity();
        player.setId(1L);
        player.setDeck(new DeckEntity());
        CardEntity card = new CardEntity();
        card.setId(42L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(cardRepo.findById(42L)).thenReturn(Optional.of(card));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deckService.addCard(43L, 1L));
    }
}