package fontys.magiccardgame;

import fontys.magiccardgame.business.CardService;
import fontys.magiccardgame.business.dto.GetAllCardsResponse;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    public void getById_shouldReturnAPresentCard() {
        CardEntity card = new CardEntity();
        when(cardsRepo.findById(1L)).thenReturn(Optional.of(card));

        Card result = cardService.getById(1L);

        assertEquals(result.getId(), card.getId());
        verify(cardsRepo, times(1)).findById(1L);
    }

    @Test
    public void getById_shouldThrowCardNotFoundExceptionWhenCardIsNotPresent() {
        when(cardsRepo.findById(2L)).thenThrow(new CardNotFoundException(2L));

        assertThrows(CardNotFoundException.class, () -> {
            cardService.getById(2L);
        });
        verify(cardsRepo, times(1)).findById(2L);
    }

    @Test
    public void saveCard_ShouldSaveCardSuccessfully() {
        Card card = Card.builder()
                .id(1L)
                .name("default")
                .attackPoints(2)
                .healthPoints(3)
                .build();
        CardEntity cardEntity = CardEntity.builder()
                .id(1L)
                .name("default")
                .attackPoints(2)
                .healthPoints(3)
                .build();
        when(cardsRepo.save(any(CardEntity.class))).thenReturn(cardEntity);

        Card result = cardService.save(card);

        assertEquals(card, result);
        verify(cardsRepo, times(1)).save(any(CardEntity.class));
    }

    @Test
    public void updateCard_ShouldReturnCorrectlyUpdatedCard() {
        // Prepare existing and updated card data
        CardEntity existingCard = CardEntity.builder()
                .id(1L)
                .name("Old Card")
                .attackPoints(10)
                .healthPoints(20)
                .build();

        Card updatedCard = Card.builder()
                .id(1L)
                .name("Updated Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();

        when(cardsRepo.findById(1L)).thenReturn(Optional.of(existingCard));

        ArgumentCaptor<CardEntity> captor = ArgumentCaptor.forClass(CardEntity.class);

        Card result = cardService.updateCard(updatedCard);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Card");
        assertThat(result.getAttackPoints()).isEqualTo(15);
        assertThat(result.getHealthPoints()).isEqualTo(25);

        verify(cardsRepo, times(1)).save(captor.capture());

        CardEntity savedCardEntity = captor.getValue();
        assertEquals(updatedCard.getId(), savedCardEntity.getId());
        assertEquals(updatedCard.getName(), savedCardEntity.getName());
        assertEquals(updatedCard.getAttackPoints(), savedCardEntity.getAttackPoints());
        assertEquals(updatedCard.getHealthPoints(), savedCardEntity.getHealthPoints());

        verify(cardsRepo, times(1)).findById(1L);
    }

    @Test
    public void updateCard_ShouldThrowIllegalArgumentException_WhenCardIdIsNotFound() {
        Card updatedCard = Card.builder()
                .id(2L)
                .name("Updated Card")
                .attackPoints(15)
                .healthPoints(25)
                .build();
        when(cardsRepo.findById(2L)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> cardService.updateCard(updatedCard));
    }

    @Test
    public void testDeleteById() {
        cardService.deleteById(1L);

        verify(cardsRepo, times(1)).deleteById(1L);
    }
}
