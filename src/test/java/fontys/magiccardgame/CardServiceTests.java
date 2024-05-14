package fontys.magiccardgame;

import fontys.magiccardgame.business.CardService;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CardServiceTests {
    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardsRepo;

    @Test
    public void getAllCards_shouldReturn_allCardsInTheCollection() {
        CardEntity card1 = new CardEntity();
        CardEntity card2 = new CardEntity();
        when(cardsRepo.findAll()).thenReturn(Arrays.asList(card1, card2));

        GetAllCardsResponse result = cardService.getAllCards();

        assertEquals(2, result.getCards().size());
        verify(cardsRepo, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        CardEntity card = new CardEntity();
        when(cardsRepo.findById(1L)).thenReturn(Optional.of(card));

        Card result = cardService.getById(1L);

        assertEquals(result.getId(),card.getId());
        verify(cardsRepo, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        CardEntity card = new CardEntity();
        when(cardsRepo.save(card)).thenReturn(card);

        CardEntity result = cardService.save(card);

        assertEquals(card, result);
        verify(cardsRepo, times(1)).save(card);
    }

    @Test
    public void testDeleteById() {
        cardService.deleteById(1L);

        verify(cardsRepo, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateCard_ValidCard() {
        // Arrange
        CardEntity existingCard = new CardEntity();
        existingCard.setId(1L);
        existingCard.setName("Old Card");
        existingCard.setAttackPoints(10);
        existingCard.setHealthPoints(20);

        Card newCard = new Card();
        newCard.setId(1L);
        newCard.setName("Updated Card");
        newCard.setAp(15);
        newCard.setHp(25);

        when(cardsRepo.findById(1L)).thenReturn(Optional.of(existingCard));

        // Act
        Card result = cardService.updateCard(newCard);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Card");
        assertThat(result.getAp()).isEqualTo(15);
        assertThat(result.getHp()).isEqualTo(25);
    }

    @Test
    public void testUpdateCard_CardNotFound() {
        // Arrange
        Card newCard = new Card();
        newCard.setId(2L); // Non-existent card ID

        when(cardsRepo.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cardService.updateCard(newCard));
    }
}
