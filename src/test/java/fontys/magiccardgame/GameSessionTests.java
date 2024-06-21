package fontys.magiccardgame;

import fontys.magiccardgame.business.GameSession;
import fontys.magiccardgame.domain.Card;
import fontys.magiccardgame.domain.Deck;
import fontys.magiccardgame.domain.PlayCardRequest;
import fontys.magiccardgame.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameSessionTests {

    private Player player1;
    private Player player2;

    private GameSession gameSession;

    @BeforeEach
    void setUp() {
        // Setup initial conditions for player1
        List<Card> hand1 = new ArrayList<>();

        List<Card> deckCards = new ArrayList<>(Arrays.asList(
                Card.builder().id(1L).name("Card1").healthPoints(5).attackPoints(3).build(),
                Card.builder().id(2L).name("Card2").healthPoints(4).attackPoints(2).build(),
                Card.builder().id(3L).name("Card2").healthPoints(4).attackPoints(2).build(),
                Card.builder().id(4L).name("Card2").healthPoints(4).attackPoints(2).build(),
                Card.builder().id(5L).name("Card2").healthPoints(4).attackPoints(2).build()
        ));
        Deck deck1 = Deck.builder().id(1L).cards(deckCards).playerId(1L).build();
        player1 = Player.builder().id(1L).deck(deck1).hand(hand1).hp(20).userId(1L).build();


        // Setup initial conditions for player2
        List<Card> hand2 = new ArrayList<>();
        player2 = Player.builder().id(2L).deck(deck1).hand(hand2).hp(20).userId(2L).build();

        // Initial draw
        gameSession = new GameSession(player1, player2);
    }

    @Test
    void testInitialDraw() {
        assertEquals(5, player1.getHand().size());
        assertEquals(5, player2.getHand().size());
    }

    @Test
    void testAddPlayCardRequest() {
        Card card1 = Card.builder().id(1L).name("Card1").healthPoints(5).attackPoints(3).build();
        Card card2 = Card.builder().id(2L).name("Card2").healthPoints(4).attackPoints(2).build();
        PlayCardRequest request1 = new PlayCardRequest();
        PlayCardRequest request2 = new PlayCardRequest();
        request1.setUserId(1L);
        request1.setCard(card1);
        request2.setUserId(2L);
        request2.setCard(card2);

        gameSession.addPlayCardRequest(request1);
        assertThrows(IllegalStateException.class, () -> gameSession.addPlayCardRequest(request1));

        gameSession.addPlayCardRequest(request2);
        assertEquals(2, gameSession.getPendingRequests().size());
        assertTrue(gameSession.isTurnFinished());
    }

    @Test
    void gameSession_ShouldExecuteTurnAndReturnRightOutput_WhenInputIsProper() {
        Card card1 = Card.builder().id(1L).name("Card1").healthPoints(5).attackPoints(5).build();
        Card card2 = Card.builder().id(2L).name("Card2").healthPoints(4).attackPoints(2).build();

        PlayCardRequest request1 = new PlayCardRequest();
        PlayCardRequest request2 = new PlayCardRequest();
        request1.setUserId(1L);
        request1.setCard(card1);
        request2.setUserId(2L);
        request2.setCard(card2);
        gameSession.addPlayCardRequest(request1);
        gameSession.addPlayCardRequest(request2);

        assertEquals(19, player2.getHp());
        assertTrue(gameSession.isTurnFinished());
    }

    @Test
    void gameShouldEnd_WhenAPlayerRunsOutOfHP() {
        player1.setHp(0);

        // Add requests to trigger turn execution
        Card card1 = Card.builder().id(1L).name("Card1").healthPoints(5).attackPoints(3).build();
        Card card2 = Card.builder().id(2L).name("Card2").healthPoints(4).attackPoints(2).build();

        PlayCardRequest request1 = new PlayCardRequest();
        PlayCardRequest request2 = new PlayCardRequest();
        request1.setUserId(1L);
        request1.setCard(card1);
        request2.setUserId(2L);
        request2.setCard(card2);

        gameSession.addPlayCardRequest(request1);
        gameSession.addPlayCardRequest(request2);

        gameSession.executeTurn();

        assertTrue(gameSession.isGameOver());
        assertNotNull(gameSession.getWinner());
        assertEquals(gameSession.getWinner(), player2);
    }

    @Test
    void gameShouldEnd_WhenAPlayerRunsOutOfCards() {
        while (!player1.getDeckStack().isEmpty()) {
            player1.getDeckStack().pop();
        }
        while (!player1.getHand().isEmpty()) {
            player1.getHand().remove(0);
        }

        // Add requests to trigger turn execution
        Card card1 = Card.builder().id(1L).name("Card1").healthPoints(5).attackPoints(3).build();
        Card card2 = Card.builder().id(2L).name("Card2").healthPoints(4).attackPoints(2).build();

        PlayCardRequest request1 = new PlayCardRequest();
        PlayCardRequest request2 = new PlayCardRequest();
        request1.setUserId(1L);
        request1.setCard(card1);
        request2.setUserId(2L);
        request2.setCard(card2);

        gameSession.addPlayCardRequest(request1);
        gameSession.addPlayCardRequest(request2);

        gameSession.executeTurn();

        assertTrue(gameSession.isGameOver());
        assertNotNull(gameSession.getWinner());
        assertEquals(gameSession.getWinner(), player2);
    }

    @Test
    void gameShouldResultInADraw_WhenTheTwoPlayersHaveNoHP() {
        player1.setHp(0);
        player2.setHp(0);

        // Add requests to trigger turn execution
        Card card1 = Card.builder().id(1L).name("Card1").healthPoints(5).attackPoints(3).build();
        Card card2 = Card.builder().id(2L).name("Card2").healthPoints(4).attackPoints(2).build();

        PlayCardRequest request1 = new PlayCardRequest();
        PlayCardRequest request2 = new PlayCardRequest();
        request1.setUserId(1L);
        request1.setCard(card1);
        request2.setUserId(2L);
        request2.setCard(card2);

        gameSession.addPlayCardRequest(request1);
        gameSession.addPlayCardRequest(request2);

        gameSession.executeTurn();

        assertTrue(gameSession.isGameOver());
        assertNull(gameSession.getWinner());
    }
}
