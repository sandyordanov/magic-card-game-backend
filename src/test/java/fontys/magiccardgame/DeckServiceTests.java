package fontys.magiccardgame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.PlayerRepo;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.Deck;
import fontys.magiccardgame.persistence.entity.Player;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DeckServiceTests {

    @Autowired
    private DeckService deckService;

    @MockBean
    private PlayerRepo playerRepo;

    @MockBean
    private CardRepository cardRepo;
    @Test
    public void testAddCard_CardNotOwnedByPlayer() {
        // Arrange
        Player player = new Player();
        player.setId(1);
        player.setDeck(new Deck());
        CardEntity card = new CardEntity();
        card.setId(42);

        when(playerRepo.findById(1)).thenReturn(Optional.of(player));
        when(cardRepo.findById(42)).thenReturn(Optional.of(card));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deckService.addCard(43, 1));
    }
}