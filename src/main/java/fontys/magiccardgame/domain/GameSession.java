package fontys.magiccardgame.domain;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private final long id;

    private Player player1;
    private Player player2;
    private boolean isGameOver = false;
    private boolean turnFinished = false;
    private Player winner;

    // Store the card requests for each player
    private final ConcurrentHashMap<Long, PlayCardRequest> pendingRequests = new ConcurrentHashMap<>();
    private final Set<Long> playersWhoPlayed = ConcurrentHashMap.newKeySet();

    public GameSession(Player player1, Player player2) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setHp(20);
        this.player2.setHp(20);
        this.player1.setHand(new ArrayList<>());
        this.player2.setHand(new ArrayList<>());

        this.player1.setDeckStack(new ArrayDeque<>(player1.getDeck().getCards()));
        this.player2.setDeckStack(new ArrayDeque<>(player2.getDeck().getCards()));
        drawInitialCards();
    }

    private void drawInitialCards() {
        for (int i = 0; i < 5; i++) {
            player1.getHand().add(player1.getDeckStack().pop());
            player2.getHand().add(player2.getDeckStack().pop());
        }
    }

    public synchronized void addPlayCardRequest(PlayCardRequest request) {
        if (playersWhoPlayed.contains(request.getUserId())) {
            throw new IllegalStateException("Player has already played a card this turn.");
        }
        pendingRequests.put(request.getUserId(), request);
        playersWhoPlayed.add(request.getUserId());
        turnFinished = false;
        if (pendingRequests.size() == 2) {
            executeTurn();
        }
    }

    private void executeTurn() {
        PlayCardRequest request1 = pendingRequests.get(player1.getUserId());
        PlayCardRequest request2 = pendingRequests.get(player2.getUserId());

        // Extract the cards being played
        Card card1 = request1.getCard();
        Card card2 = request2.getCard();

        // Calculate the health points after the attack
        int card1RemainingHp = card1.getHealthPoints() - card2.getAttackPoints();
        int card2RemainingHp = card2.getHealthPoints() - card1.getAttackPoints();

        // Apply remaining damage to players if card HP goes negative
        if (card1RemainingHp < 0) {
            player1.setHp(player1.getHp() + card1RemainingHp); // card1RemainingHp is negative
        }
        if (card2RemainingHp < 0) {
            player2.setHp(player2.getHp() + card2RemainingHp); // card2RemainingHp is negative
        }
        // Remove played cards from players' hands
        player1.getHand().remove(card1);
        player2.getHand().remove(card2);

        // Draw new cards from deckStack
        if (!player1.getDeckStack().isEmpty()) {
            Card newCard1 = player1.getDeckStack().pop();
            player1.getHand().add(newCard1);
        }

        if (!player2.getDeckStack().isEmpty()) {
            Card newCard2 = player2.getDeckStack().pop();
            player2.getHand().add(newCard2);
        }
        if (checkGameOver()) {
            isGameOver = true;
            if (player1.getHp()<=0 && player2.getHp()<=0){
                winner = null;
            }
            if (player1.getHp()>0 || player2.getHand().isEmpty()){
                winner = player1;
            }
            if (player2.getHp()> 0 || player1.getHand().isEmpty()){
                winner = player2;
            }
            turnFinished = true;
            return;
        }

        turnFinished = true;

    }

    private boolean checkGameOver() {
        if (player1.getHp()<=0 || player2.getHp()<=0){
            return true;
        }
        if(player1.getHand().isEmpty() || player2.getHand().isEmpty()){
            return true;
        }
        return false;
    }
}
