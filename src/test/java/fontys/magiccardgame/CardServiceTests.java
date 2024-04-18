package fontys.magiccardgame;

import fontys.magiccardgame.business.CardService;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.entity.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@SpringBootTest
public class CardServiceTests {
    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardsRepo;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCards() {
        Card card1 = new Card();
        Card card2 = new Card();
        when(cardsRepo.findAll()).thenReturn(Arrays.asList(card1, card2));

        List<Card> result = cardService.getAllCards();

        assertEquals(2, result.size());
        verify(cardsRepo, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        Card card = new Card();
        when(cardsRepo.findById(1)).thenReturn(Optional.of(card));

        Optional<Card> result = cardService.getById(1);

        assertTrue(result.isPresent());
        verify(cardsRepo, times(1)).findById(1);
    }

    @Test
    public void testSave() {
        Card card = new Card();
        when(cardsRepo.save(card)).thenReturn(card);

        Card result = cardService.save(card);

        assertEquals(card, result);
        verify(cardsRepo, times(1)).save(card);
    }

    @Test
    public void testDeleteById() {
        cardService.deleteById(1);

        verify(cardsRepo, times(1)).deleteById(1);
    }

    @Test
    public void testUpdateCard() {
        Card card = new Card();
        card.setId(1);
        when(cardsRepo.existsById(card.getId())).thenReturn(true);
        when(cardsRepo.findById(card.getId())).thenReturn(Optional.of(card));

        boolean result = cardService.updateCard(card);

        assertTrue(result);
        verify(cardsRepo, times(1)).existsById(card.getId());
        verify(cardsRepo, times(1)).findById(card.getId());
    }
}
