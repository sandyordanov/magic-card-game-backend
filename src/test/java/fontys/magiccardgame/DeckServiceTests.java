package fontys.magiccardgame;

import fontys.magiccardgame.business.DeckService;
import fontys.magiccardgame.business.dto.AddCardToDeckRequest;
import fontys.magiccardgame.business.exception.CardNotFoundException;
import fontys.magiccardgame.business.exception.DeckSizeLimitException;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.persistence.CardRepository;
import fontys.magiccardgame.persistence.PlayerRepository;
import fontys.magiccardgame.persistence.entity.CardEntity;
import fontys.magiccardgame.persistence.entity.DeckEntity;
import fontys.magiccardgame.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DeckServiceTests {

    @InjectMocks
    private DeckService deckService;
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CardRepository cardRepo;
    @Test
    public void addCard_ShouldAddCardToThePlayersDeck() {
        AddCardToDeckRequest request = AddCardToDeckRequest.builder().cardId(1L).playerId(1L).build();
        DeckEntity deck = DeckEntity.builder()
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
                .build();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));

        deckService.addCard(request);
        verify(playerRepository).findById(1L);
        verify(cardRepo).findById(1L);
        verify(playerRepository).save(player);

        assertTrue(player.getDeck().getCards().contains(card));
    }
    @Test
    public void addCard_ShouldThrowAnException_WhenCardIdIsNotValid() {
        AddCardToDeckRequest request = AddCardToDeckRequest.builder().cardId(1L).playerId(1L).build();
        PlayerEntity player = PlayerEntity.builder()
                .id(1L)
                .deck(new DeckEntity())
                .ownedCards(new ArrayList<>())
                .build();
        when(cardRepo.findById(1L)).thenThrow(CardNotFoundException.class);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThrows(CardNotFoundException.class, ()->{
            deckService.addCard(request);
        });
    }
    @Test
    public void addCard_ShouldThrowAnException_WhenPlayerIdIsNotValid() {
        AddCardToDeckRequest request = AddCardToDeckRequest.builder().playerId(1L).build();

        when(playerRepository.findById(1L)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, ()->{
            deckService.addCard(request);
        });
    }
    @Test
    public void addCard_ShouldThrowAnException_WhenDeckSizeIsExceeded() {
        AddCardToDeckRequest request = AddCardToDeckRequest.builder().cardId(1L).playerId(1L).build();
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
                .cards(cards)
                .build();
        PlayerEntity player = PlayerEntity.builder()
                .id(1L)
                .deck(deck)
                .ownedCards(new ArrayList<>())
                .build();
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        assertThrows(DeckSizeLimitException.class, ()->{
            deckService.addCard(request);
        });
    }
    @Test
    public void addCard_ShouldThrowAnException_WhenPlayerDoesNotPossessTheCard() {
        AddCardToDeckRequest request = AddCardToDeckRequest.builder().cardId(1L).playerId(1L).build();
        DeckEntity deck = DeckEntity.builder()
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
                .build();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(IllegalArgumentException.class, ()->{
            deckService.addCard(request);
        });
    }
}